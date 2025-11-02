package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserRepository extends BaseRepository<User> implements UserStorage {
    JdbcTemplate jdbcTemplate;
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY)" +
            " VALUES (?, ?, ?, ?)";
    private static final String ADD_FRIEND = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID)" +
            " VALUES(?, ?)";
    private static final String FIND_ALL_FRIENDS = "SELECT * FROM users" +
             " WHERE id IN (select friend_id from friends WHERE user_id = ?)";
    private static final String FIND_ALL_FRIENDS_IDS = "SELECT * FROM users where id in " +
            "(SELECT friend_id FROM friends where user_id = ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends" +
            " WHERE user_id = ? and friend_id = ?";

    public UserRepository(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
        jdbcTemplate = jdbc;
    }

    //private long userId = 0;
    //private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getById(long id) {
        //String sqlQuery1 = "SELECT * FROM users WHERE id = ?";

        //User user = jdbcTemplate.queryForObject(sqlQuery1, new UserRowMapper(), id);


        //return user;

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
        //int id = insert(
                INSERT_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );

        user.setId(id);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        validation(user);

        update(
                UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = findMany(FIND_ALL_QUERY);
        log.info("Получение всех пользователей: {}", users);
        return users;
    }

    public User addFriend(Long id, Long friendId) {
        User user = getById(id);
        User userFriend = getById(friendId);

        Friends friend = new Friends();
        friend.setId(friendId);

        Set<Friends> UserFriends = user.getFriends();
        //Set<User> UserFriends = user.getFriends();
        //UserFriends.add(UserFriend);
        UserFriends.add(friend);
        user.setFriends(UserFriends);

        //
 /*         friend = new Friends();
        friend.setId(id);
        UserFriends = userFriend.getFriends();
        UserFriends.add(friend);
        userFriend.setFriends(UserFriends);
*/

        long rowid = insert(
                ADD_FRIEND,
                user.getId(),
                userFriend.getId()
        );

/*        rowid = insert(
                ADD_FRIEND,
                userFriend.getId(),
                user.getId()
        );*/

        log.info("Пользователь {} дружит с {}", user, userFriend);

        return user;
    }

    public Collection<User> getFriends(Long id) {
        Optional<User> optionalUser = findOne(FIND_BY_ID_QUERY, id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + id + " не существует");
        }

        Collection<User> users = findMany(FIND_ALL_FRIENDS, id);
        //if (users.isEmpty()) {
        //    throw new NotFoundException("У пользователя с id: " + id + " нет друзей");
        //}
        return users;
    }

    public User removeFriend(Long id, Long friendId) {
        /*User user = getById(id);
        Set<Friends> friends = user.getFriends();
        Friends friend = new Friends();
        friend.setId(friendId);
        friends.remove(friend);*/

        Optional<User> optionalUserFriend = findOne( FIND_BY_ID_QUERY, friendId);
        if (optionalUserFriend.isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + friendId + " не существует");
        }

        delete(
                DELETE_FRIEND,
                id,
                friendId
        );

        return getById(id);

    }

    public Collection<User> getCommonFriends(long id, long otherId) {

        //User user = getUser(id);
        Optional<User> optionalUser = findOne(FIND_BY_ID_QUERY, id);
        User user = optionalUser.get();

        Collection<User> userFriends = findMany(FIND_ALL_FRIENDS_IDS, id);

        Set<Long> userIds = userFriends.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        Set<Friends> friends = new HashSet<>();
        for (Long l : userIds) {
            Friends friend = new Friends();
            friend.setId(l);
            friends.add(friend);
        }

        user.setFriends(friends);
        System.out.println("user.setFriends: " + user);

        //
        Optional<User> optionalOtherUser = findOne(FIND_BY_ID_QUERY, otherId);
        User otherUser = optionalOtherUser.get();

        userFriends = findMany(FIND_ALL_FRIENDS_IDS, otherId);

        userIds = userFriends.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        friends = new HashSet<>();
        for (Long l : userIds) {
            Friends friend = new Friends();
            friend.setId(l);
            friends.add(friend);
        }

        otherUser.setFriends(friends);
        System.out.println("otherUser.setFriends: " + otherUser);

        return user.findCommonFriends(otherUser).stream()
                .map(this::getById)
                .collect(Collectors.toList());
        //return user.findCommonFriends(otherUser);

        //return user.findCommonFriends(OtherUser).stream();

    }


    protected /*long*/int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
                System.out.println(params[idx]);
            }
            return ps;}, keyHolder);

        //Long id = (Long) keyHolder.getKeyAs(Long.class);
        int id = keyHolder.getKeyAs(Integer.class);
        //Optional<Integer> id = keyHolder.getKeyAs(Integer.class);
        //keyHolder.
        return id;

/*
        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }*/
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
            //throw new handleNotFoundException("Не удалось обновить данные");
            /*Exception e = new NotFoundException("Не удалось обновить данные");
            throw new ErrorResponse(
                    e.toString(),
                    "404";*/
            //throw new ErrorResponse("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        //if (rowsUpdated == 0) {
        //    throw new InternalServerException("Не удалось удалить данные");
        //}
        //if (rowsUpdated == 0) {
        //    throw new InternalServerException("Не удалось обновить данные");

            //throw new handleNotFoundException("Не удалось обновить данные");
            /*Exception e = new NotFoundException("Не удалось обновить данные");
            throw new ErrorResponse(
                    e.toString(),
                    "404";*/
            //throw new ErrorResponse("Не удалось обновить данные");

        //}
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
