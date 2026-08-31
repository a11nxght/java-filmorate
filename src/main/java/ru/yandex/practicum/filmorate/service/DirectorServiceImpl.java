package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage directorStorage;

    @Override
    public Director add(Director director) {
        log.info("Start adding director");
        long id = directorStorage.save(director);
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director director) {
        log.info("Start updating director");
        directorStorage.findById(director.getId())
                .orElseThrow(() -> new NotFoundException("Режиссер с id: " + director.getId() + " не найден"));
        directorStorage.update(director);
        return director;
    }

    @Override
    public void delete(long id) {
        log.info("Start deleting director");
        directorStorage.delete(id);
    }

    @Override
    public Director findById(long id) {
        log.info("Start getting director");
        return directorStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер с id: " + id + " не найден"));
    }

    @Override
    public List<Director> findAll() {
        log.info("Start getting all directors");
        return directorStorage.findAll();
    }
}
