package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAServiceImpl implements MPAService {

    private final MPAStorage mpaStorage;

    @Override
    public MPA findById(long id) {
        log.info("Fetching MPA with id: {}", id);
        return mpaStorage.findById(id).orElseThrow(() -> new NotFoundException("MPA с id: " + id + " - не найден."));
    }

    @Override
    public List<MPA> findAll() {
        log.info("Fetching all MPA");
        return mpaStorage.findAll();
    }
}
