package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        log.debug("Создание нового пользователя");
        userStorage.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Обновление пользователя {}", user.getId());
        userStorage.update(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        log.debug("Получение всех пользователей");
        return userStorage.getAll();
    }

    @Override
    public void addFriend(int id, int friendId) {
        log.debug("Добавление друга {} для пользователя {}", friendId, id);

        Optional<User> userOpt = userStorage.getById(id);
        Optional<User> friendOpt = userStorage.getById(friendId);

        if (!userOpt.isPresent() || !friendOpt.isPresent()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        User user = userOpt.get();
        User friend = friendOpt.get();

        // Добавляем дружбу в обе стороны
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        // Сохраняем изменения для обоих пользователей
        userStorage.update(user);
        userStorage.update(friend);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        log.debug("Удаление друга {} у пользователя {}", friendId, id);

        Optional<User> userOpt = userStorage.getById(id);
        Optional<User> friendOpt = userStorage.getById(friendId);

        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        User user = userOpt.get();
        User friend = friendOpt.get();

        // Удаляем дружбу в обе стороны
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        // Сохраняем изменения для обоих пользователей
        userStorage.update(user);
        userStorage.update(friend);
    }

    @Override
    public Set<Integer> getFriends(int id) {
        log.debug("Получение списка друзей для пользователя {}", id);
        return userStorage.getById(id)
                .map(User::getFriends)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    public Set<Integer> getCommonFriends(int id, int otherId) {
        log.debug("Поиск общих друзей между пользователями {} и {}", id, otherId);

        Set<Integer> friends1 = getFriends(id);
        Set<Integer> friends2 = getFriends(otherId);

        Set<Integer> commonFriends = new HashSet<>(friends1);
        commonFriends.retainAll(friends2);

        return commonFriends;
    }
}