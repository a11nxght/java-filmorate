package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    Director add(Director director);

    Director update(Director director);

    void delete(long id);

    Director findById(long id);

    List<Director> findAll();
}
