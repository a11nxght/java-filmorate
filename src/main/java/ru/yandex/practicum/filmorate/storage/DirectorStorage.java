package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    Optional<Director> findById(long id);

    List<Director> findAll();

    long save(Director director);

    void update(Director director);

    void delete(long id);

    List<Director> findFilmDirectors(long id);
}
