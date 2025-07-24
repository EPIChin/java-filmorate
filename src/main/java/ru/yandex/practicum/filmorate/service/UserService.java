package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    User create(User user);

    User update(User user);

    List<User> findAll();

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    Set<Integer> getFriends(int id);

    Set<Integer> getCommonFriends(int id, int otherId);
}
