package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUpEach() {
        filmController = new FilmController();
    }

    @Test
    void add() {
        addOneFilm();
        Film film2 = Film.builder()
                .name("Terminator2")
                .description("Ogon'")
                .releaseDate(LocalDate.of(1888, 1, 2))
                .duration(3600)
                .build();
        Film film3 = Film.builder()
                .name("Terminator2")
                .description("Ogon'")
                .releaseDate(LocalDate.of(2000, 1, 2))
                .duration(-3600)
                .build();
        ValidationException firstValidationException = assertThrows(ValidationException.class,
                () -> filmController.add(film2));
        ValidationException secondValidationException = assertThrows(ValidationException.class,
                () -> filmController.add(film3));
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года.", firstValidationException.getMessage());
        assertEquals("Продолжительность фильма должна быть положительным числом.", secondValidationException.getMessage());
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    void update() {
        addOneFilm();
        Film updFilm1 = Film.builder()
                .id(222)
                .name("Terminator2")
                .description("Ogon'")
                .releaseDate(LocalDate.of(1888, 1, 2))
                .duration(3600)
                .build();
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> filmController.update(updFilm1));
        assertEquals("Фильм с таким id не найден.",  notFoundException.getMessage());
    }

    private void addOneFilm() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Ogon'")
                .releaseDate(LocalDate.of(1999, 1, 2))
                .duration(3600)
                .build();
        filmController.add(film);
    }


}