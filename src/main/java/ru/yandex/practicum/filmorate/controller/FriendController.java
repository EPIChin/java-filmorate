package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;


import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class FriendController {
    private final UserService userService;
    private final UserStorage userStorage;

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Добавление друга {} для пользователя {}", friendId, id);

        User user = userStorage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Удаление друга {} у пользователя {}", friendId, id);

        User user = userStorage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        userService.removeFriend(id, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<Friend>> getFriends(@PathVariable int id) {
        log.info("Получение списка друзей для пользователя {}", id);

        User user = userStorage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Set<Integer> friends = userService.getFriends(id);

        List<Friend> friendResponses = friends.stream()
                .map(Friend::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(friendResponses);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<Friend>> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) {
        log.info("Поиск общих друзей между пользователями {} и {}", id, otherId);

        User user = userStorage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        User otherUser = userStorage.getById(otherId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь 2 не найден"));


        Set<Integer> commonFriends = userService.getCommonFriends(id, otherId);

        List<Friend> friendResponses = commonFriends.stream()
                .map(Friend::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(friendResponses);
    }
}
