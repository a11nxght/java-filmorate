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

    List<Film> findPopularWithGenreAndYear(int count, long genreId, int year);

    List<Film> findPopularWithGenre(int count, long genreId);

    List<Film> findPopularWithYear(int count, int year);

    List<Film> findCommon(long userId, long friendId);

    List<Film> findByDirector(long directorId);
}
