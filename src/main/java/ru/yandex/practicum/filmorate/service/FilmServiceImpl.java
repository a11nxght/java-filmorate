package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    @Override
    public Film add(Film film) {
        log.info("Start adding film: {}", film);
        validateFilm(film);
        return filmStorage.save(film);
    }

    @Override
    public Film update(Film film) {
        log.info("Start updating film: {}", film);
        filmStorage.findById(film.getId()).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        validateFilm(film);
        return filmStorage.update(film);
    }

    @Override
    public void delete(long id) {
        log.info("Start deleting film with id: {}", id);
        filmStorage.delete(id);
    }

    @Override
    public Film findById(long id) {
        log.info("Start finding film with id: {}", id);
        Film film = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        filmStorage.setFilmGenres(film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.info("Start finding all films");
        return filmStorage.findAll();
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Start adding like to film with id: {} from user with id: {}", filmId,  userId);
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.info("Start remove like to film with id: {} from user with id: {}", filmId,  userId);
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> findPopular(int count) {
        log.info("Start getting popular films");
        return filmStorage.findPopular(count);
    }

    @Override
    public List<Film> findCommonFilms(long userId, long friendId) {
        log.info("Start getting common films");
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " - не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " - не найден."));
        return filmStorage.findCommon(userId, friendId);
    }

    private void validateFilmDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film date: {} is not valid", film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }

    private void validateFilmDuration(Film film) throws ValidationException {
        if (film.getDuration() < 0) {
            log.warn("Film duration: {} is not valid", film.getDuration());
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
