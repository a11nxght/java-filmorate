package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private long id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов.")
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int likes;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MPA mpa;
    private Set<Director> directors = new HashSet<>();
}
