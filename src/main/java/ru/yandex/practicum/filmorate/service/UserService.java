package ru.yandex.practicum.filmorate.service;

import java.util.Set;

public interface UserService {

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    Set<Integer> getFriends(int id);

    Set<Integer> getCommonFriends(int id, int otherId);
}
