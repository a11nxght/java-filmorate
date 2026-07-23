package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private FilmServiceImpl filmService;
    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    void setUpEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceImpl(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void add() {
        addOneFilm();
        Film film2 = new Film();
        film2.setName("Terminator2");
        film2.setDescription("Ogon'");
        film2.setReleaseDate(LocalDate.of(1888, 1, 2));
        film2.setDuration(3600);
        Film film3 = new Film();
        film3.setName("Terminator2");
        film3.setDescription("Ogon'");
        film3.setReleaseDate(LocalDate.of(2000, 1, 2));
        film3.setDuration(-3600);
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
        Film updFilm1 = new Film();
        updFilm1.setId(222);
        updFilm1.setName("Terminator2");
        updFilm1.setDescription("Ogon'");
        updFilm1.setReleaseDate(LocalDate.of(1888, 1, 2));
        updFilm1.setDuration(3600);
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> filmController.update(updFilm1));
        assertEquals("Фильм с таким id не найден.", notFoundException.getMessage());
    }

    private void addOneFilm() {
        Film film = new Film();
        film.setName("Terminator");
        film.setDescription("Ogon'");
        film.setReleaseDate(LocalDate.of(1999, 1, 2));
        film.setDuration(3600);
        filmController.add(film);
    }


}