package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class Film {
    long id;
    @NotBlank(message = "Имя не должно быть пустым.")
    String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов.")
    String description;

    LocalDate releaseDate;
    int duration;
}
