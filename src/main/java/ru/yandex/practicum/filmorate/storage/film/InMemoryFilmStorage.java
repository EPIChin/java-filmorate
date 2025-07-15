package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public void save(Film film) {
        if (film.getId() == null) {
            film.setId(getNextId());
        }
        films.put(film.getId(), film);
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

    @Override
    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResourceNotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    private int getNextId() {
        return films.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}
