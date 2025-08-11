package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
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

    private List<Genre> genres;
    private Rating mpa;
    @JsonIgnore
    @Builder.Default
    private Set<Long> likes = new HashSet<>();

    public void addLike(long userId) {
        this.likes.add(userId);
    }

    public void deleteLike(long userId) {
        this.likes.remove(userId);
    }
}
