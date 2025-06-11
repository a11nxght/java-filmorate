package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film newFilm);

    Film delete(Film film);

    Collection<Film> getAll();

    Film getFilmById(Long id);
}
