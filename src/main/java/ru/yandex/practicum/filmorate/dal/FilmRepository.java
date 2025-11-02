package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    public static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT id, NAME, description, releasedate, duration, mpa FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (NAME, description, releasedate, duration, mpa)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILMS_GENRES_LINK_QUERY = "INSERT INTO films_genres_link (film_id, genre_id)" +
            " VALUES (?, ?)";
    private static final String UPDATE_ALL_FIELDS = "UPDATE films" +
            " SET name = ?, releaseDate =?, description = ?, duration = ? WHERE id = ?";
    private static final String FIND_COUNT_QUERY = "SELECT * FROM films LIMIT ?";
    private static final String DELETE_LIKE = "DELETE FROM likes" +
            " WHERE film_id = ? and user_id = ?";

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
        jdbcTemplate = jdbc;
    }

    @Override
    public Film getById(long id) {

        Optional<Film> film =  findOne(FIND_BY_ID_QUERY, id);

        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }

        String sqlQueryGenres = "SELECT id, name FROM genres WHERE id IN (SELECT genre_id FROM films_genres_link WHERE film_id = ?)";
        List<Genres> genres = jdbcTemplate.query(sqlQueryGenres, new GenreRowMapper(), film.get().getId());
        film.get().setGenres(genres);
        film.get().setDuration(Duration.ofSeconds(film.get().getDuration().getSeconds() / 1440));

        String sqlQueryMpa = "SELECT mpa FROM films WHERE id = ?";
        Long idFilm = jdbcTemplate.queryForObject(sqlQueryMpa, new LongRowMapper<Long>(), film.get().getId());

        String sqlQueryMpaObject = "SELECT * FROM MPA WHERE id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sqlQueryMpaObject, new MpaRowMapper(), idFilm);

        film.get().setMpa(mpa);

        log.info("Запрос фильма : {}", film.get());
        return film.get();
    }

    @Override
    public Film create(Film film) {
        validation(film);

        long id;
        try {
            id = insert(
                    INSERT_QUERY,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration().toMinutes(), // TO DO
                    film.getMpa().getId()//,
            );

        film.setId(id);

        if (film.getGenres() != null) {
            List<Genres> genres = film.getGenres();
            for (Genres genre : genres) {
                insert(INSERT_FILMS_GENRES_LINK_QUERY, film.getId(), genre.getId());
            }
        }
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Ошибка при сохранении фильма: " + e.getMessage());
        }

        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    public Film removeLike(long userId, long filmId) {

        delete(
                DELETE_LIKE,
                filmId,
                userId
        );
        Film film = getById(filmId);

        log.info("У фильма: {} удален like от пользователя с id: {}", film, userId);
        return film;
    }

    @Override
    public Film update(Film film) {

        update(
                UPDATE_ALL_FIELDS,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getId()
        );
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        log.info("Получение всех фильмов: {}", films);
        return films;
    }

    public Collection<Film> getMostPopular(long count) {
        Collection<Film> films = findMany(FIND_COUNT_QUERY, count);

        String sqlQueryLikes = "SELECT user_id FROM likes WHERE film_id = ?";
        for (Film film : films) {
            film.setLikes(new HashSet<>(jdbcTemplate.queryForList(sqlQueryLikes, Long.class, film.getId())));
        }

        films = films.stream().sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed()).limit(count)
                .collect(Collectors.toList());

        log.info("Получение всех фильмов: {}", films);
        return films;
    }

    public Film addLike(Long userId, Long filmId) {
        Optional<Film> optionalFilm = findOne(FIND_BY_ID_QUERY, filmId);

        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        Film film = optionalFilm.get();

        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);

        insert("INSERT INTO likes (film_id, user_id) VALUES(?, ?)", filmId, userId);

        log.info("Пользователь с id: {} поставил like фильму: {}", userId, film);
        return film;
    }

    protected int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;}, keyHolder);

        int id = keyHolder.getKeyAs(Integer.class);

        return id;
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

    private static void validation(Film film) {

        if (film.getName().isEmpty()) {
            log.warn("Название не может быть пустым {}", film);
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов. Длина описания {} - Описание: {}", film.getDescription().length(), film.getDescription());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года - Дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isZero() || film.getDuration().isNegative()) {
            log.warn("Продолжительность фильма должна быть положительным числом. Продолжительность: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
