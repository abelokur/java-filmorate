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

    public User addFriend(Long id, Long friendId) {

        User user = getUser(id);
        User friend =  getUser(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        inMemoryUserStorage.update(user);
        inMemoryUserStorage.update(friend);

        return user;
    }

    public User removeFriend(long id, long friendId) {

        User user = getUser(id);
        User friend = getUser(friendId);

        Set<Long> userFriends = user.getFriends();
        userFriends.remove(friend.getId());
        user.setFriends(userFriends);

        userFriends = friend.getFriends();
        userFriends.remove(user.getId());
        friend.setFriends(userFriends);

        inMemoryUserStorage.update(user);

        return user;
    }

    public Collection<User> getAllFriends(long id) {

        //return getUser(id).getFriends();
        User user = getUser(id);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());

    }

    public User getUserById(long id) {
        return getUser(id);
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);

        return user.findCommonFriends(otherUser).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
        /*
        User user = getUser(id);
        User otherUser = getUser(otherId);

        return user.findCommonFriends(otherUser);*/
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
