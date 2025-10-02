package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class User {

    private long id;

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public Set<Long> findCommonFriends(User otherUser) {
        Set<Long> commonFriends = new HashSet<>(this.friends);

        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends;
    }

}
