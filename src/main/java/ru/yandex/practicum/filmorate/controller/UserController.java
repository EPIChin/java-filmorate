package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserStorage userStorage;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("POST /user/{}", user.getLogin());
        try {
            userStorage.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("PUT /user/{}", user.getLogin());
        try {
            userStorage.update(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Ошибка при обновлении пользователя");
        }
    }

    @GetMapping
    public List<User> findAll() {
        log.info("GET /users");
        return userStorage.getAll();
    }
}