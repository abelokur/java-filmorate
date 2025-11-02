package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import java.text.ParseException;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmRepository.getById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {
        return filmRepository.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        return filmRepository.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {

        return filmRepository.findAll();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long userId, @PathVariable long id) {
        return filmRepository.removeLike(userId, id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long userId, @PathVariable long id) {
        return filmRepository.addLike(userId, id);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopular(@RequestParam(defaultValue = "10") long count) {
        return filmRepository.getMostPopular(count);
    }
}
