package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaRepository mpaRepository;

    @Autowired
    public MpaController(MpaRepository mpaRepository) {

        this.mpaRepository = mpaRepository;
    }

    @GetMapping
    public ResponseEntity<Collection<Mpa>> findAll() {
        return ResponseEntity.ok(mpaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getMpa(@PathVariable long id) {
        return ResponseEntity.ok(mpaRepository.getById(id));
    }
}
