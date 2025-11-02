package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongRowMapper<L extends Number> implements RowMapper<Long>  {
    @Override
    public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long mpa;
        mpa = (resultSet.getLong("mpa"));

        return mpa;
    }
}
