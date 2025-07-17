package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void save(User user);

    void delete(long id);

    void update(User user);

    List<User> getAll();

    Optional<User> getById(long id);
}
