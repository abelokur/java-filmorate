package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {

        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;

    }

    public Film addLike(long userId, long filmId) {

        Film film = getFilm(filmId);
        User user = getUser(userId);

        Set<Long> filmLikes = film.getLikes();

        filmLikes.add(user.getId());

        film.setLikes(filmLikes);

        return film;
    }

    public Film removeLike(long userId, long filmId) {

        Film film = getFilm(filmId);
        User user = getUser(userId);

        Set<Long> filmLikes = film.getLikes();

        filmLikes.remove(user.getId());

        film.setLikes(filmLikes);

        return film;
    }

    public Set<Film> getMostPopular(long count) {
        return inMemoryFilmStorage
                .findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed()).limit(count)//.toString();
                .collect(Collectors.toSet());
    }

    private Film getFilm(long filmId) {

        Optional<Film> film = inMemoryFilmStorage
                .findAll()
                .stream()
                .filter(f -> f.getId() == filmId)
                .findFirst();

        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }

        return film.get();
    }

    private User getUser(long userId) {

        Optional<User> user = inMemoryUserStorage
                .findAll()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst();

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        return user.get();
    }
}
