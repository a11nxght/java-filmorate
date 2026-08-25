package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film add(Film film);

    Film update(Film film);

    void delete(long id);

    Film findById(long id);

    List<Film> findAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> findPopular(int count);

    List<Film> findPopularWithGenreAndYear(int count, long genreId, int year);

    List<Film> findPopularWithGenre(int count, long genreId);

    List<Film> findPopularWithYear(int count, int year);

    List<Film> findCommonFilms(long userId, long friendId);

    List<Film> findDirectorFilms(long directorId, List<String> sortBy);
}
