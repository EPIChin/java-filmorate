package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.*;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Введите название фильма.")
    private String name;

    @NotNull
    @Size(max = 200, message = "Слишком длинное описание.")
    private String description;

    @NotNull
    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше 0.")
    private Integer duration;

    private Set<Integer> likedUsers = new HashSet<>();

    public int getLikedUsersSize() {
        return likedUsers.size();
    }
    /*
    @NotNull
    private Mpa mpa;
    private final LinkedHashSet<Genre> genres = new LinkedHashSet<>();*/
}