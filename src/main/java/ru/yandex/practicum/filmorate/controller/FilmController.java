package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("films")
public class FilmController {

    private final FilmService filmService;

    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        log.info("Update film: {}", updateFilm);
        filmService.update(updateFilm);
        log.info("Film updated");
        return updateFilm;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Adding film: {}", film);
        Film addingFilm = filmService.add(film);
        log.info("Added film with id: {}", addingFilm.getId());
        return addingFilm;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Fetching all films");
        return filmService.findAll();
    }

    @PutMapping("{id}/like/{userId}")
    public void like(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("User with id: {} likes film with id: {}", userId, id);
        filmService.addLike(id, userId);
        log.info("Liked film");
    }

    @DeleteMapping("{id}/like/{userId}")
    public void unlike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("User with id: {} unlikes film with id: {}", userId, id);
        filmService.removeLike(id, userId);
        log.info("Unliked film");
    }

    @GetMapping("popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count,
                                            @RequestParam(required = false) Long genreId,
                                            @RequestParam(required = false) Integer year) {
        log.info("Fetching popular films");
        if (genreId != null && year != null) {
            return filmService.findPopularWithGenreAndYear(count, genreId, year);
        } else if (genreId != null) {
            return filmService.findPopularWithGenre(count, genreId);
        } else if (year != null) {
            return filmService.findPopularWithYear(count, year);
        }  else {
            return filmService.findPopular(count);
        }
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable long id) {
        log.info("Fetching film with id: {}", id);
        return filmService.findById(id);
    }

    @GetMapping("common")
    public Collection<Film> getCommonFilms(@RequestParam long userId,
                                           @RequestParam long friendId) {
        log.info("Fetching common films");
        return filmService.findCommonFilms(userId, friendId);
    }

    @DeleteMapping("{filmId}")
    public void delete(@PathVariable long filmId) {
        log.info("Deleting film with id: {}", filmId);
        filmService.delete(filmId);
        log.info("Film deleted");
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getDirectorFilms(@PathVariable long directorId,
                                             @RequestParam(required = false) List<String> sortBy) {
        log.info("Get director with id: {} films", directorId);
        return filmService.findDirectorFilms(directorId, sortBy);
    }

//    GET /fimls/search
    @GetMapping("search")
    public Collection<Film> searchFilms(@RequestParam String query,
                                        @RequestParam List<String> by) {
        log.info("Search films with query: {}", query);
        return filmService.searchFilms(query, by);
    }

}
