package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(username, email, password, registration_date)" +
            "VALUES (?, ?, ?, ?) returning id";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    private long userId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getById(long id) {

        //Optional<User> user = Optional.ofNullable(users.get(id));
        Optional<User> user =  findOne(FIND_BY_ID_QUERY, id);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }

        log.info("Запрос пользователя : {}", user.get());
        return user.get();
    }

    @Override
    public User create(User user) {
        validation(user);

        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getFriends()
        );

        user.setId(id);
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

        update(
                UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getId()
        );

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

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;}, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
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
