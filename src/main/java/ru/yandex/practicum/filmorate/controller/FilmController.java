package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId;


    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {
        validation(film);

        film.setId(++filmId);
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        Film updateFilm = films.get(film.getId());

        updateFilm.setName(film.getName());
        updateFilm.setDescription(film.getDescription());
        updateFilm.setReleaseDate(film.getReleaseDate());
        updateFilm.setDuration(film.getDuration());

        log.info("Обновлен фильм: {}", film);
        return updateFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {

        log.info("Получение всех фильмов: {}", films.values());
        return films.values();
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
