package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));

        //TO DO
        //как добавить друзей?

        return mpa;
    }
}
