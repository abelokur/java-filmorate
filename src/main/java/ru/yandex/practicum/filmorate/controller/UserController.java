package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    //private final UserStorage userStorage;
    //private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(/*UserStorage userStorage, UserService userService,*/ UserRepository userRepository) {

        //this.userStorage = userStorage;
        //this.userService = userService;
        this.userRepository = userRepository;
    }
    /*
    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userStorage.getById(id);
    }*/
    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userRepository.getById(id);
    }
    /*
    @PostMapping
    public User create(@RequestBody User user) {
        System.out.println("User create(@RequestBody User user)");
        return userStorage.create(user);
    }*/
    @PostMapping
    public User create(@RequestBody User user) {
        System.out.println("User create(@RequestBody User user)");
        return userRepository.create(user);
    }
   /*
    @PutMapping
    public User update(@RequestBody User user) {

        return userStorage.update(user);
    }
   */
   @PutMapping
   public User update(@RequestBody User user) {

       return userRepository.update(user);
   }
   /*
    @GetMapping
    public Collection<User> findAll() {

        return userStorage.findAll();
    }*/
    @GetMapping
    public Collection<User> findAll() {

        return userRepository.findAll();
    }

    /*
    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }*/
    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable long id, @PathVariable long friendId) {
        return userRepository.addFriend(id, friendId);
    }
/*
    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }
    */
    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        return userRepository.removeFriend(id, friendId);
    }


    /*
    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }*/
    @  GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable long id) {
        return userRepository.getFriends(id);
    }
    /*

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId); // список друзей, общих с другим пользователем
    }
    */
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userRepository.getCommonFriends(id, otherId); // список друзей, общих с другим пользователем
    }
}
