package ru.yandex.practicum.filmorate.storage.film;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(getNextId());
        }
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public void update(User user) {
        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setLogin(user.getLogin());
        existingUser.setName(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
        existingUser.setBirthday(user.getBirthday());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(int id) {

        return Optional.ofNullable(users.get(id));
    }

    private int getNextId() {
        return users.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}
