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

    private Set<Friends> friends = new HashSet<>();

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

    private Set<Long> getFriendsId() {
        Set<Long> friendsIds = new HashSet<>();

        for (Friends friend : this.getFriends()) {
            friendsIds.add(friend.getId());
        }

        return friendsIds;
    }

    public Set<Long> findCommonFriends(User otherUser) {

        Set<Long> commonFriends = this.getFriendsId();//new HashSet<>();
        commonFriends.retainAll(otherUser.getFriendsId());

        return commonFriends;
    }
}
