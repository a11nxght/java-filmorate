package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    public void validationTest() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
        FilmController filmController = new FilmController(inMemoryFilmStorage, filmService);
        assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            film.setName("terminator");
            film.setDescription("Пятеро друзей ( комик-группа «Шарло»), " +
                    "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал " +
                    "им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал " +
                    "кандидатом Коломбани.");
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            film.setName("terminator");
            film.setDescription("terminator");
            film.setReleaseDate(LocalDate.of(1894, 1, 1));
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            film.setName("terminator");
            film.setDescription("terminator");
            film.setReleaseDate(LocalDate.of(1994, 1, 1));
            film.setDuration(0);
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
    }
}