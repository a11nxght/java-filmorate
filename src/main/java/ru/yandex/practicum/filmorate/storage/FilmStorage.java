package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film create(Film film);

    public Film update(Film newFilm);

    public Film delete(Film film);

    public Collection<Film> getAll();

    public Film getFilmById(Long id);
}
