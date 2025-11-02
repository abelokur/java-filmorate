package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;

    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUserById(long id) {
        return getUser(id);
    }

    public User getCommonFriends1(long id, long otherId) {

        User user = getUser(id);
        User otherUser = getUser(otherId);

        return user;
    }

    private User getUser(long id) {

        Optional<User> user = inMemoryUserStorage
                .findAll()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        return user.get();
    }
}
