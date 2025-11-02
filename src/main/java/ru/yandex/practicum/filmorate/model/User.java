package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class User {

    private long id;

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    //private Set<User> friends = new HashSet<>();
    private Set<Friends> friends = new HashSet<>();
    //private Set<Long> friends = new HashSet<>();
    //private List<User> friends = new ArrayList<>();

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
    //public Set<Friends> findCommonFriends(User otherUser) {

        Set<Long> commonFriends = this.getFriendsId();//new HashSet<>();

        System.out.println("Set<Long> commonFriends: " + commonFriends);

        System.out.println("otherUser.getFriendsId(): " + otherUser.getFriendsId());

        commonFriends.retainAll(otherUser.getFriendsId());
        System.out.println("return commonFriends: " + commonFriends);
        return commonFriends;
        /*Set<Friends> user1Friends = this.getFriends();
        Set<Friends> user2Friends = otherUser.getFriends();
        return user1Friends.stream()
                .filter(user2Friends::contains)
                .collect(Collectors.toSet());
        */
        /*Set<Long> commonFriends = new HashSet<>();
        //Set<Long> commonFriends = new HashSet<>(this.friends.);
        Set<Long> friends = new HashSet<>();// = this.getFriends(this.getFriends())
        friends.addAll(this.getFriends());
        commonFriends.addAll()

        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends;*/
    }

}
