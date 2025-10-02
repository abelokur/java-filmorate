package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {

        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {

        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        return inMemoryFilmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {

        return inMemoryFilmStorage.findAll();
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
