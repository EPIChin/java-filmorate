package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.*;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

import java.time.LocalDate;


@Data
public class Film {
    private Integer id;

    @NotBlank(message = "Введите название фильма.")
    private String name;

    @NotNull
    @Size(max = 200, message = "Слишком длинное описание.")
    private String description;

    @NotNull
    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Проолжительность фильма должна быть больше 0.")
    private Integer duration;
}