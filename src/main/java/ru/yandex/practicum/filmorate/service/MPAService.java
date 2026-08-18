package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAService {
    MPA findById(long id);

    List<MPA> findAll();
}
