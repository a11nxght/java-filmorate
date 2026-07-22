package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        films.remove(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }
        films.put(film.getId(), film);
        log.info("Film updated: {}", film);
        return film;
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    private long getNextId() {
        return films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0)
                + 1;
    }
}
