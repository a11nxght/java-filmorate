package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film save(Film film);

    void delete(long id);

    Film update(Film film);

    Optional<Film> findById(long id);

    List<Film> findAll();

    List<Film> findPopular(int count);

    List<Film> findCommon(long userId, long friendId);
}
