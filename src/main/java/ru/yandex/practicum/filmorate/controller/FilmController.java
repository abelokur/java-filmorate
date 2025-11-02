package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
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

    //private final FilmStorage filmStorage;
    //private final FilmService filmService;
    private final FilmRepository filmRepository;

    @Autowired
    public FilmController(/*FilmStorage filmStorage,*//* FilmService filmService, */FilmRepository filmRepository) {

        //this.filmStorage = filmStorage;
        //this.filmService = filmService;
        this.filmRepository = filmRepository;
    }

    /*
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmStorage.getById(id);
    }
    */
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmRepository.getById(id);
    }

    /*
    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {

        return filmStorage.create(film);
    }
    */

    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {
        System.out.println("film:film");
        System.out.println("film: " + film);
        return filmRepository.create(film);
    }

    /*
    @PutMapping
    public Film update(@RequestBody Film film) {

        return filmStorage.update(film);
    }
    */
    @PutMapping
    public Film update(@RequestBody Film film) {

        return filmRepository.update(film);
    }

    /*
    @GetMapping
    public Collection<Film> findAll() {

        return filmStorage.findAll();
    }
    */
    @GetMapping
    public Collection<Film> findAll() {

        return filmRepository.findAll();
    }

    /*
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long userId, @PathVariable long id) {
        return filmService.addLike(userId, id);
    }
    */
    /*
    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long userId, @PathVariable long id) {
        return filmService.removeLike(userId, id);
    }
    */

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long userId, @PathVariable long id) {
        return filmRepository.removeLike(userId, id);
    }
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long userId, @PathVariable long id) {
        return filmRepository.addLike(userId, id);
    }
    /*
    @GetMapping("/popular")
    public Set<Film> getMostPopular(@RequestParam(defaultValue = "10") long count) {
        return filmService.getMostPopular(count);
    }
    */
    @GetMapping("/popular")
    public Collection<Film> getMostPopular(@RequestParam(defaultValue = "10") long count) {
        return filmRepository.getMostPopular(count);
    }
}
