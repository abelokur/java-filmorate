package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    //private UserController userController;
    private InMemoryUserStorage userController;
    private User validUser;
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @BeforeEach
    void setUp() {
        //userController = new InMemoryUserStorage(inMemoryUserStorage, userService);
        userController = new InMemoryUserStorage();
        validUser = Stub.getUser();
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage);
    }

    @Test
    @DisplayName("Создание пользователя")
    void test_CreateUser() {
        //given
        User createdUser = userController.create(validUser);
        //when

        //then
        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getLogin());
        assertEquals("testUser name", createdUser.getName());
        assertEquals("testUser@test.ru", createdUser.getEmail());
        assertTrue(createdUser.getId() > 0);
    }

    @Test
    @DisplayName("Электронная почта должна содержать символ @")
    void test_CheckEmail() {
        //given
        validUser.setEmail("testUsertest.ru");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(validUser));
        //then
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    @DisplayName("Электронная почта не может быть пустой")
    void test_CheckEmptyEmail() {
        //given
        validUser.setEmail("");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(validUser));
        //then
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    @DisplayName("Логин не может быть пустым")
    void test_CheckEmptyLogin() {
        //given
        validUser.setLogin("");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(validUser));
        //then
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    @DisplayName("Логин не может содержать пробелы")
    void test_CheckLogin() {
        //given
        validUser.setLogin("test User");

        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(validUser));
        //then
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    @DisplayName("Дата рождения не может быть в будущем")
    void test_CheckBirthday() {
        //given
        validUser.setBirthday(LocalDate.now().plusDays(1));
        System.out.println(validUser.getBirthday());
        //when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(validUser));
        //then
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    static class Stub {
        public static User getUser() {
            User user = new User();
            user.setId(1);
            user.setLogin("testUser");
            user.setName("testUser name");
            user.setEmail("testUser@test.ru");
            user.setBirthday(LocalDate.of(2000, 1, 1));

            return user;
        }
    }
}
