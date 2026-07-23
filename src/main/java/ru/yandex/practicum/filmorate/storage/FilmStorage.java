package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    void delete(long id);

    Film update(Film film);

    Optional<Film> get(long id);

    Collection<Film> getAll();
}
