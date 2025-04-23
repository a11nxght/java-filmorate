package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        isFilmValid(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            isFilmValid(newFilm);
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Обновлен фильм с ID {}", oldFilm.getId());
            return oldFilm;
        }
        log.warn("Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Film delete(Film film) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Нет фильма с id %d", id));
        }
        return films.get(id);
    }

    private void isFilmValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("название не может быть пустым");
            throw new ValidationException("Has error response");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("максимальная длина описания — 200 символов");
            throw new ValidationException("Has error response");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Has error response");
        }
        if (film.getDuration() <= 0) {
            log.warn("продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Has error response");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}
