package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    public void validationTest() {
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> {
            Film film = new Film(null, null, null, null, 0);
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film(null, "terminator", "Пятеро друзей ( комик-группа «Шарло»), " +
                    "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал " +
                    "им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал " +
                    "кандидатом Коломбани.", null, 0);
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film(null, "terminator", "terminator",
                    LocalDate.of(1894, 1, 1), 0);
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            Film film = new Film(null, "terminator", "terminator",
                    LocalDate.of(1994, 1, 1), 0);
            filmController.create(film);
        }, "Должно выводиться исключение валидации");
    }
}