package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (Objects.isNull(film.getLikes())) {
            film.setLikes(new HashSet<>());
        }
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (Objects.isNull(film.getLikes())) {
            return;
        }
        film.getLikes().remove(userId);
    }

    public Collection<Film> getTopFilms(String limit) {
        try {
            int filmsLimit = Integer.parseInt(limit);
            if (filmsLimit < 0) {
                throw new NumberFormatException();
            }
            return filmStorage.getAll()
                    .stream()
                    .peek(film -> {
                        if (Objects.isNull(film.getLikes())) {
                            film.setLikes(new HashSet<>());
                        }
                    })
                    .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                    .limit(filmsLimit).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("Параметр count должен быть целым числом");
        }
    }
}
