package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreRepository extends BaseRepository<Genres>{
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID = "SELECT * FROM genres WHERE id = ?";
    public GenreRepository(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genres> findAll() {
        Collection<Genres> genres = findMany(FIND_ALL_QUERY);
        log.info("Получение всех жанров: {}", genres);
        return genres;
    }

    public Genres getById(Long id) {
        Optional<Genres> genre =  findOne(FIND_BY_ID, id);

        if (genre.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }

        log.info("Запрос жанра : {}", genre.get());
        return genre.get();
    }

}
