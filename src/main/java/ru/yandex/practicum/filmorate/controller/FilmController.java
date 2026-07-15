package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        if (films.containsKey(updateFilm.getId())) {
            Film film = films.get(updateFilm.getId());
            film.setName(updateFilm.getName());
            if (updateFilm.getDescription() != null && !updateFilm.getDescription().isBlank()) {
                film.setDescription(updateFilm.getDescription());
            }
            if (updateFilm.getReleaseDate() != null) {
                validateFilmDate(updateFilm);
                film.setReleaseDate(updateFilm.getReleaseDate());
            }
            if (updateFilm.getDuration() != 0) {
                validateFilmDuration(updateFilm);
                film.setDuration(updateFilm.getDuration());
            }
            log.info("Film updated: {}", film);
            return film;
        }
        log.info("Film not found: {}", updateFilm);
        throw new NotFoundException("Фильм с таким id не найден.");
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        validateFilmDate(film);
        validateFilmDuration(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private long getNextId() {
        return films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0)
                + 1;
    }

    private void validateFilmDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Film date: {} is not valid", film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }

    private void validateFilmDuration(Film film) throws ValidationException {
        if (film.getDuration() < 0) {
            log.debug("Film duration: {} is not valid", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

}
