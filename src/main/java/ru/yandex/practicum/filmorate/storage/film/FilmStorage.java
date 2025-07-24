package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void save(Film film);

    void delete(long id);

    void update(Film film);

    List<Film> getAll();

    Optional<Film> getById(long id);
}
