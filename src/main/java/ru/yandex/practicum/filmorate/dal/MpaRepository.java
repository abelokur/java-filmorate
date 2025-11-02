package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> findAll() {
        Collection<Mpa> mpas = findMany(FIND_ALL_QUERY);
        log.info("Получение списка рейтингов: {}", mpas);
        return mpas;
    }

    public Mpa getById(long id) {

        Optional<Mpa> mpa =  findOne(FIND_BY_ID_QUERY, id);

        if (mpa.isEmpty()) {
            throw new NotFoundException("Рейтинг с id: " + id + " не найден");
        }

        log.info("Запрос рейтинга : {}", mpa.get());
        return mpa.get();
    }
}
