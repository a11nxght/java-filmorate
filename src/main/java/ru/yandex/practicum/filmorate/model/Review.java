package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    Long reviewId;
    @NotBlank(message = "Текст отзыва не может быть пустым.")
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
    long useful;
}
