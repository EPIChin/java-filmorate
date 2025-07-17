package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(Integer filmId);

    boolean addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getMostPopularFilms(int count);
}