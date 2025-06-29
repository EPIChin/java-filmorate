package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("POST / film / {}", film.getName());

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getReleaseDate() == null) {
            film.setReleaseDate(LocalDate.from(Instant.now()));
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть больше нуля");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film newFilm) {
        log.info("PUT / film / {}", newFilm.getName());

        if (newFilm.getId() == null) {
            throw new ValidationException("ID фильма обязателен");
        }

        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        Film oldFilm = films.get(newFilm.getId());

        if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
            if (newFilm.getName().length() < 2) {
                throw new ValidationException("Название должно содержать минимум 2 символа");
            }
            oldFilm.setName(newFilm.getName());
        }

        if (newFilm.getDescription() != null) {
            if (newFilm.getDescription().isBlank()) {
                throw new ValidationException("Описание не может быть пустым");
            }
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null) {
            if (newFilm.getDuration() <= 0) {
                throw new ValidationException("Продолжительность должна быть больше нуля");
            }
            oldFilm.setDuration(newFilm.getDuration());
        }

        return ResponseEntity.ok(oldFilm);
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("GET / films");
        return new ArrayList<>(films.values());
    }
}
