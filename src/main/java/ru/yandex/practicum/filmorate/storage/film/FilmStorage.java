package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    // добавление
    public Film create(@RequestBody Film film);

    // модификация
    public Film update(@RequestBody Film film);

    // удаление

    // поиск объектов
    public Collection<Film> findAll();
}
