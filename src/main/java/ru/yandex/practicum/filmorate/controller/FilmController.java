package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Поступил запрос на создание фильма");
        Film createdFilm = filmStorage.create(film);
        log.info("Cоздан фильм с ID: {}", film.getId());
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Поступил запрос на обновление фильма");
        Film updatedFilm = filmStorage.update(newFilm);
        log.info("Обновлен фильм с ID: {}", updatedFilm.getId());
        return updatedFilm;
    }
}
