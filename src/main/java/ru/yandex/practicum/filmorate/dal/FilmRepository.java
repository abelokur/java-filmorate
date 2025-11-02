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
import ru.yandex.practicum.filmorate.model.User;
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
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (NAME, description, releasedate, duration, mpa)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILMS_GENRES_LINK_QUERY = "INSERT INTO films_genres_link (film_id, genre_id)" +
            " VALUES (?, ?)";
    private static final String UPDATE_ALL_FIELDS = "UPDATE films" +
            " SET name = ?, releaseDate =?, description = ?, duration = ? WHERE id = ?";
    private static final String FIND_COUNT_QUERY = "SELECT * FROM films LIMIT ?";
    //private static final String FIND_FILM_WITH_MPA = "SELECT * FROM films WHERE id = ? AND "
    private static final String DELETE_LIKE = "DELETE FROM likes" +
            " WHERE film_id = ? and user_id = ?";

    private final Map<Long, Film> films = new HashMap<>();
    private long filmId;

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
        jdbcTemplate = jdbc;
    }

    @Override
    public Film getById(long id) {

        //Optional<Film> film = Optional.ofNullable(films.get(id));
        Optional<Film> film =  findOne(FIND_BY_ID_QUERY, id);

        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }

        String sqlQueryFilm = "SELECT id, NAME, description, releasedate, duration, mpa FROM films WHERE id = ?";
        Film film1 = jdbcTemplate.queryForObject(sqlQueryFilm, new FilmRowMapper(), film.get().getId());
        System.out.println("film1:: " + film1);

        String sqlQueryGenres = "SELECT id, name FROM genres WHERE id IN (SELECT genre_id FROM films_genres_link WHERE film_id = ?)";

        List<Genres> genres = jdbcTemplate.query(sqlQueryGenres, new GenreRowMapper(), film.get().getId());

        film.get().setGenres(genres);

        film.get().setDuration(Duration.ofSeconds(film.get().getDuration().getSeconds() / 1440));

/*        System.out.println("film.get().getId(): " + film.get().getId());
        String sqlQueryFilm = "SELECT * FROM films WHERE id = ?"; //"SELECT mpa FROM films WHERE id = ?";
        //Film film_mpa = jdbcTemplate.queryForObject(sqlQueryFilm,  new FilmRowMapper(), film.get().getId());
        Film film_mpa = jdbcTemplate.queryForObject(sqlQueryFilm,  new FilmRowMapper(), film.get().getId());
        System.out.println("film_mpa:: " + film_mpa);
*/
        //
        String sqlQueryMpa = "SELECT mpa FROM films WHERE id = ?";
        //jdbcTemplate.
        //Long l_id = jdbcTemplate.queryForObject(sqlQueryMpa, new LongRowMapper<Long>(), film_mpa.getId());
        Long id_film = jdbcTemplate.queryForObject(sqlQueryMpa, new LongRowMapper<Long>(), film.get().getId());
        System.out.println("l_id:: " + id_film);


        //System.out.println("film_mpa.getMpa(): " + film_mpa.getMpa().getId());
        String sqlQueryMpaObject = "SELECT * FROM MPA WHERE id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sqlQueryMpaObject, new MpaRowMapper(), id_film);
        System.out.println("mpa::: " + mpa);

        //String sqlQueryMpa = "SELECT id, name FROM mpa WHERE id in (SELECT mpa FROM films WHERE id = ?)";
        //String sqlQueryMpa = "SELECT id, name FROM mpa WHERE id = 192";
        //Mpa mpa = jdbcTemplate.queryForObject(sqlQueryMpa, new MpaRowMapper(), film.get().getId());
        //film.get().setMpa(mpa);

        //film.get().setMpa(mpa);

        film.get().setMpa(mpa);

        log.info("Запрос фильма : {}", film.get());
        return film.get();
    }

    @Override
    public Film create(Film film) {
        validation(film);

        long id; // id вставки в таблицу films
        try {
            id = insert(
                    //int id = insert(
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
                System.out.println("film.getId(): " + film.getId() + " -|- " + "genre.getId(): " + genre.getId());
                long id_genres = insert(INSERT_FILMS_GENRES_LINK_QUERY, film.getId(), genre.getId());
            }
        }
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Ошибка при сохранении фильма: " + e.getMessage());
        }
        System.out.println("film::: " + film);

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

        //String sqlQueryFilm = "SELECT * FROM users WHERE id = ?";
        //User user = jdbcTemplate.queryForObject(sqlQueryFilm, new UserRowMapper(), userId);

        return film;

    }

    /*
    @Override
    public Film create(Film film) {
        validation(film);

        film.setId(++filmId);
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм: {}", film);
        return film;
    }
    */

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

    /*
    @Override
    public Film update(Film film) {
        if (film == null) {
            throw new NotFoundException("Фильм не указан");
        }

        Film updateFilm = films.get(film.getId());

        if (updateFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }

        updateFilm.setName(film.getName());
        updateFilm.setDescription(film.getDescription());
        updateFilm.setReleaseDate(film.getReleaseDate());
        updateFilm.setDuration(film.getDuration());

        log.info("Обновлен фильм: {}", film);
        return updateFilm;
    }
    */

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        log.info("Получение всех фильмов: {}", films);
        return films;
    }

    public Collection<Film> getMostPopular(long count) {
        Collection<Film> films = findMany(FIND_COUNT_QUERY, count).stream().sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed()).limit(count)
                .collect(Collectors.toSet());

        String sqlQueryLikes = "SELECT user_id FROM likes WHERE film_id = ?";
        for (Film film : films) {
            film.setLikes(new HashSet<>(jdbcTemplate.queryForList(sqlQueryLikes, Long.class, film.getId())));
        }
        log.info("Получение всех фильмов: {}", films);
        return films;
    }

    public Film addLike(Long userId, Long filmId) {
        Optional<Film> optionalFilm = findOne(FIND_BY_ID_QUERY, filmId);
        //Optional<User> user = findOne(FIND_USER_BY_ID_QUERY, userId);
        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        //if (user.isEmpty()) {
        //    throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        //}

        Film film = optionalFilm.get();
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);

        long id_genres = insert("INSERT INTO likes (film_id, user_id)" +
                " VALUES(?, ?)", filmId, userId);

        log.info("Пользователь с id: {} поставил like фильму: {}", userId, film);
        return film;
    }

    protected /*long*/int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
                System.out.println(params[idx]);
            }
            return ps;}, keyHolder);

        //Long id = (Long) keyHolder.getKeyAs(Long.class);
        int id = keyHolder.getKeyAs(Integer.class);
        //Optional<Integer> id = keyHolder.getKeyAs(Integer.class);
        //keyHolder.
        return id;

/*
        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }*/
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
            //throw new handleNotFoundException("Не удалось обновить данные");
            /*Exception e = new NotFoundException("Не удалось обновить данные");
            throw new ErrorResponse(
                    e.toString(),
                    "404";*/
            //throw new ErrorResponse("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        //if (rowsUpdated == 0) {
        //    throw new InternalServerException("Не удалось удалить данные");
        //}
        //if (rowsUpdated == 0) {
        //    throw new InternalServerException("Не удалось обновить данные");

        //throw new handleNotFoundException("Не удалось обновить данные");
            /*Exception e = new NotFoundException("Не удалось обновить данные");
            throw new ErrorResponse(
                    e.toString(),
                    "404";*/
        //throw new ErrorResponse("Не удалось обновить данные");

        //}
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
