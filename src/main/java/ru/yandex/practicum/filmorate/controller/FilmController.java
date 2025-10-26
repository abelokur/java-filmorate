package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {

        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmStorage.getById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {

        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {

        return filmStorage.findAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long userId, @PathVariable long id) {
        return filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long userId, @PathVariable long id) {
        return filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public Set<Film> getMostPopular(@RequestParam(defaultValue = "10") long count) {
        return filmService.getMostPopular(count);
    }
}
