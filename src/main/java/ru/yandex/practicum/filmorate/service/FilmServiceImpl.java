package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film add(Film film) {
        validateFilmDate(film);
        validateFilmDuration(film);
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        filmStorage.get(film.getId()).orElseThrow(() -> new ValidationException("Film not found"));
        validateFilmDate(film);
        validateFilmDuration(film);
        return filmStorage.update(film);
    }

    @Override
    public void delete(long id) {
        filmStorage.delete(id);
    }

    @Override
    public Film findById(long id) {
        return filmStorage.get(id).orElseThrow(() -> new ValidationException("Film not found"));
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(Film::getId))
                .toList();
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId).orElseThrow(() -> new ValidationException("Film not found"));
        User user = userStorage.get(userId).orElseThrow(() -> new ValidationException("User not found"));
        film.getLikes().add(user.getId());
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId).orElseThrow(() -> new ValidationException("Film not found"));
        User user = userStorage.get(userId).orElseThrow(() -> new ValidationException("User not found"));
        film.getLikes().remove(user.getId());
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(count)
                .toList();
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
}
