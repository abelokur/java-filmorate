package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getFilm(@PathVariable long id) {
        return ResponseEntity.ok(filmRepository.getById(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Film film) throws ParseException {
        return ResponseEntity.ok(filmRepository.create(film));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Film film) {

        return ResponseEntity.ok(filmRepository.update(film));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> findAll() {

        return ResponseEntity.ok(filmRepository.findAll());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity removeLike(@PathVariable long userId, @PathVariable long id) {
        return ResponseEntity.ok(filmRepository.removeLike(userId, id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity addLike(@PathVariable long userId, @PathVariable long id) {
        return ResponseEntity.ok(filmRepository.addLike(userId, id));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getMostPopular(@RequestParam(defaultValue = "10") long count) {
        return ResponseEntity.ok(filmRepository.getMostPopular(count));
    }
}
