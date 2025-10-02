package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {

    private long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    @JsonSerialize(using = MyDurationSerializer.class)
    private Duration duration;

    private Set<Long> likes = new HashSet<>();
}
