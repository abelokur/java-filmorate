package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    // получение пользователя по id
    public User getById(@PathVariable long id);

    // добавление
    public User create(@RequestBody User user);

    // модификация
    public User update(@RequestBody User user);

    // удаление

    // поиск объектов
    public Collection<User> findAll();

}
