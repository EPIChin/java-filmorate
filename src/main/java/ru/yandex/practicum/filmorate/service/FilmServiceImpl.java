package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        log.info("Попытка добавить лайк фильму {} пользователем {}", filmId, userId);

        Optional<User> userOpt = userStorage.getById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        return filmStorage.getById(filmId)
                .map(film -> {
                    if (!film.getLikedUsers().contains(userId)) {
                        film.getLikedUsers().add(userId);
                        film.setLikesCount(film.getLikesCount() + 1);
                        filmStorage.update(film);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        log.info("Попытка удалить лайк фильма {} пользователем {}", filmId, userId);

        Optional<User> userOpt = userStorage.getById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        return filmStorage.getById(filmId)
                .map(film -> {
                    if (film.getLikedUsers().remove(userId)) {
                        film.setLikesCount(film.getLikesCount() - 1);
                        filmStorage.update(film);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public List<Film> getMostPopularFilms() {
        log.info("Получение топ-10 самых популярных фильмов");
        return filmStorage.getAll()
                .stream()
                .sorted(Comparator.comparingLong(Film::getLikesCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
