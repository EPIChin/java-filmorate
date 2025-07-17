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
    public Film createFilm(Film film) {
        log.info("Создание нового фильма: {}", film.getName());
        filmStorage.save(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID: {}", film.getId());
        Optional<Film> existingFilm = filmStorage.getById(film.getId());
        if (existingFilm.isPresent()) {
            filmStorage.update(film);
            return film;
        }
        throw new ResourceNotFoundException("Фильм не найден");
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAll();
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        log.info("Поиск фильма по ID: {}", filmId);
        return filmStorage.getById(filmId);
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        log.info("Добавление лайка к фильму {} пользователем {}", filmId, userId);
        Optional<User> userOpt = userStorage.getById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        Film filmExc = filmStorage.getById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));

        return filmStorage.getById(filmId)
                .map(film -> {
                    if (!film.getLikedUsers().contains(userId)) {
                        film.getLikedUsers().add(userId);
                        filmStorage.update(film);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка фильма {} пользователем {}", filmId, userId);
        Optional<User> userOpt = userStorage.getById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        Film filmExc = filmStorage.getById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));

        return filmStorage.getById(filmId)
                .map(film -> {
                    if (film.getLikedUsers().remove(userId)) {
                        filmStorage.update(film);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        log.info("Получение топ-{} самых популярных фильмов", count);
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikedUsersSize).reversed()
                        .thenComparing(Film::getId))
                .limit(count)
                .collect(Collectors.toList());
    }
}