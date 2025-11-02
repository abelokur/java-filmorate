package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaRepository mpaRepository;

    @Autowired
    public MpaController(/*FilmStorage filmStorage,*//* FilmService filmService, */MpaRepository mpaRepository) {

        //this.filmStorage = filmStorage;
        //this.filmService = filmService;
        this.mpaRepository = mpaRepository;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        return mpaRepository.findAll();
    }
    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable long id) {
        return mpaRepository.getById(id);
    }
}
