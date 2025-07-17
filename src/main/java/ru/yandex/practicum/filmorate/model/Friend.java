package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {
    private Long id;

    public Friend(long id) {
        this.id = id;
    }
}
