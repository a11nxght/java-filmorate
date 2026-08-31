package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.event_feed.Event;
import ru.yandex.practicum.filmorate.model.event_feed.EventType;
import ru.yandex.practicum.filmorate.model.event_feed.Operation;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;
    private final DirectorStorage directorStorage;
    private final EventStorage eventStorage;

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
        setFilmGenres(film);
        setFilmDirectors(film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.info("Start finding all films");
        return filmStorage.findAll().stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Start adding like to film with id: {} from user with id: {}", filmId,  userId);
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.addLike(filmId, userId);
        eventStorage.addEvent(Event.builder()
                .userId(userId
                ).eventType(EventType.LIKE)
                .operation(Operation.ADD)
                .entityId(filmId)
                .build());
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.info("Start remove like to film with id: {} from user with id: {}", filmId,  userId);
        filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден."));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
        likesStorage.removeLike(filmId, userId);
        eventStorage.addEvent(Event.builder()
                .userId(userId
                ).eventType(EventType.LIKE)
                .operation(Operation.REMOVE)
                .entityId(filmId)
                .build());
    }

    @Override
    public List<Film> findPopular(int count) {
        log.info("Start getting popular films");
        return filmStorage.findPopular(count)
                .stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findPopularWithGenreAndYear(int count, long genreId, int year) {
        log.info("Start getting popular films with genre: {} and year: {}", genreId, year);
        return filmStorage.findPopularWithGenreAndYear(count, genreId, year)
                .stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findPopularWithGenre(int count, long genreId) {
        log.info("Start getting popular films with genre: {}", genreId);
        return filmStorage.findPopularWithGenre(count, genreId)
                .stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findPopularWithYear(int count, int year) {
        log.info("Start getting popular films with year: {}", year);
        return filmStorage.findPopularWithYear(count, year)
                .stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findCommonFilms(long userId, long friendId) {
        log.info("Start getting common films");
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " - не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " - не найден."));
        return filmStorage.findCommon(userId, friendId)
                .stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findDirectorFilms(long directorId, List<String> sortBy) {
        directorStorage.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссер с id: " + directorId + " - не найден."));
        List<Film> directorFilms = filmStorage.findByDirector(directorId);
        if (sortBy != null) {
            if (sortBy.contains("year")) {
                directorFilms = directorFilms.stream().sorted(Comparator.comparing(Film::getReleaseDate)).toList();
            } else if (sortBy.contains("likes")) {
                directorFilms = directorFilms.stream().sorted(Comparator.comparing(Film::getLikes).reversed()).toList();
            }
        }
        return directorFilms.stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> searchFilms(String query, List<String> by) {
        log.info("Start search films with query: {}", query);
        return filmStorage.searchFilms(query, by).stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findRecommendations(long userId) {
        return filmStorage.findRecommendations(userId).stream()
                .peek(this::setFilmGenres)
                .peek(this::setFilmDirectors)
                .collect(Collectors.toList());
    }

    private void setFilmGenres(Film film) {
        film.getGenres().addAll(genreStorage.findFilmGenres(film.getId()));
    }

    private void setFilmDirectors(Film film) {
        film.getDirectors().addAll(directorStorage.findFilmDirectors(film.getId()));
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
