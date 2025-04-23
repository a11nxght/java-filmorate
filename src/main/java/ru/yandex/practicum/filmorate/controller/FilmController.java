package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.filmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Поступил запрос на вывод всех фильмов");
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

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Поступил запрос на вывод фильма c id: {}", id);
        return filmStorage.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Поступил запрос добавление лайка фильму с ID: {}, от пользователя с ID: {}", id, userId
        );
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Поступил запрос удаление лайка у фильма с ID: {} от пользовалеля с ID: {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Поступил запрос на вывод {} популярных фильмов", count);
        return filmService.getTopFilms(count);
    }
}
