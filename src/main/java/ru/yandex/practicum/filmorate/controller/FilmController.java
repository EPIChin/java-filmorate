package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("POST /film/{}", film.getName());
        Film createdFilm = filmService.createFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film newFilm) {
        log.info("PUT /film/{}", newFilm.getName());
        Film updatedFilm = filmService.updateFilm(newFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("GET /films");
        return filmService.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer filmId,
                                        @PathVariable Integer userId) {
        log.info("Добавление лайка к фильму {} пользователем {}", filmId, userId);

        boolean success = filmService.addLike(filmId, userId);

        return success ?
                ResponseEntity.ok(filmService.getFilmById(filmId).orElseThrow()) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Integer filmId,
                                           @PathVariable Integer userId) {
        log.info("Удаление лайка фильма {} пользователем {}", filmId, userId);
        boolean success = filmService.removeLike(filmId, userId);
        return success ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }
}