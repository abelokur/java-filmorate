package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable long id) {
        return ResponseEntity.ok(userRepository.getById(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody User user) {
        System.out.println("User create(@RequestBody User user)");
        return ResponseEntity.ok(userRepository.create(user));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.update(user));
    }

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {

        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity addToFriends(@PathVariable long id, @PathVariable long friendId) {
        return ResponseEntity.ok(userRepository.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        return ResponseEntity.ok(userRepository.removeFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(@PathVariable long id) {
        return ResponseEntity.ok(userRepository.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return ResponseEntity.ok(userRepository.getCommonFriends(id, otherId)); // список друзей, общих с другим пользователем
    }
}
