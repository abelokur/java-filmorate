package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Getter
public class InMemoryUserStorage implements UserStorage {

    private long userId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        validation(user);

        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        validation(user);

        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не найден {}", user.toString());
            throw new NotFoundException("Пользователь не найден");
        }

        User updateUser = users.get(user.getId());
        updateUser.setLogin(user.getLogin());
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setBirthday(user.getBirthday());

        log.info("Обновлен пользователь: {}", user);
        return updateUser;
    }

    @Override
    public Collection<User> findAll() {

        log.info("Получение всех пользователей: {}", users.values().toString());
        if (users.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        System.out.println("public Collection<User> findAll() {");
        return users.values();
    }

    private static void validation(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @. Электронная почта {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы. Полученный логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем. Дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
