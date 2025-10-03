package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {

        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        System.out.println("User create(@RequestBody User user)");
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {

        return inMemoryUserStorage.update(user);
    }

    @GetMapping
    public Collection<User> findAll() {

        return inMemoryUserStorage.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId); // список друзей, общих с другим пользователем
    }

}
