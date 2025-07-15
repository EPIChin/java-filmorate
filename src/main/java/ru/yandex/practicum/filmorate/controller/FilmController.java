package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("POST /film/{}", film.getName());

        filmStorage.save(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film newFilm) {
        log.info("PUT /film/{}", newFilm.getName());

        Film film = filmStorage.getById(newFilm.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));

        filmStorage.update(newFilm);
        return ResponseEntity.ok(newFilm);
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("GET /films");
        return filmStorage.getAll();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer filmId,
                                        @PathVariable Integer userId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));

        boolean success = filmService.addLike(filmId, userId);

        return success ?
                ResponseEntity.ok(filmStorage.getById(filmId).orElseThrow()) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Integer filmId,
                                           @PathVariable Integer userId) {
        log.info("Удаление лайка фильма {} пользователем {}", filmId, userId);

        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));

        boolean success = filmService.removeLike(filmId, userId);
        return success ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms() {
        log.info("Получение топ-10 популярных фильмов");
        return filmService.getMostPopularFilms();
    }
}