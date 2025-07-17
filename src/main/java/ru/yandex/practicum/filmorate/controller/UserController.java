package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("POST /user/{}", user.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("PUT /user/{}", user.getLogin());
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

    @GetMapping
    public List<User> findAll() {
        log.info("GET /users");
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Добавление друга {} для пользователя {}", friendId, id);
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Удаление друга {} у пользователя {}", friendId, id);
        userService.removeFriend(id, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<Friend>> getFriends(@PathVariable int id) {
        log.info("Получение списка друзей для пользователя {}", id);
        return ResponseEntity.ok(userService.getFriends(id).stream()
                .map(Friend::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<Friend>> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) {
        log.info("Поиск общих друзей между пользователями {} и {}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId).stream()
                .map(Friend::new)
                .collect(Collectors.toList()));
    }
}