package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

@Component
public class GenreRowMapper implements RowMapper<Genres> {
    @Override
    public Genres mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Genres genre = new Genres();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));
        System.out.println("genre:: " + genre);
        //TO DO
        //как добавить друзей?

        return genre;
    }
}
