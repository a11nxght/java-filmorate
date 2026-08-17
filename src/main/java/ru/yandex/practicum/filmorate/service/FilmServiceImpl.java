package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    public FilmServiceImpl(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                           @Qualifier("UserDbStorage") UserStorage userStorage,
                           LikesStorage likesStorage,
                           GenreStorage genreStorage,
                           MPAStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film add(Film film) {
        validateFilm(film);
        log.info("Adding film: {}", film);
        return filmStorage.save(film);
    }

    @Override
    public Film update(Film film) {
        filmStorage.findById(film.getId()).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        validateFilm(film);
        log.info("Updating film: {}", film);
        return filmStorage.update(film);
    }

    @Override
    public void delete(long id) {
        filmStorage.delete(id);
    }

    @Override
    public Film findById(long id) {
        Film film = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        filmStorage.setFilmGenres(film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public void addLike(long filmId, long userId) {
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.findPopular(count);
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

    private void validateFilmGenres(Film film) throws NotFoundException {
        if (!film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre -> genreStorage.findById(genre.getId())
                    .orElseThrow(() -> new NotFoundException("Жанр с id: " + genre.getId() + " - не найден.")));
        }
    }

    private void validateFilmMpa(Film film) throws NotFoundException {
        if (film.getMpa() != null) {
            mpaStorage.findById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг с id: " + film.getMpa().getId() + " - не найден."));
        }
    }

    private void validateFilm(Film film) {
        validateFilmDate(film);
        validateFilmDuration(film);
        validateFilmGenres(film);
        validateFilmMpa(film);
    }
}
