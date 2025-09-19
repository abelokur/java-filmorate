package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    private int id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    @JsonSerialize(using = MyDurationSerializer.class)
    private Duration duration;

}
