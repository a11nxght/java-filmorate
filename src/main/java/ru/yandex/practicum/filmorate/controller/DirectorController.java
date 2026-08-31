package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public List<Director> findAll() {
        log.info("Find all directors");
        return directorService.findAll();
    }

    @GetMapping("{id}")
    public Director findById(@PathVariable Long id) {
        log.info("Find director by id: {}", id);
        return directorService.findById(id);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        log.info("Creating director: {}", director);
        Director createdDirector = directorService.add(director);
        log.info("Created director with id: {}", createdDirector.getId());
        return createdDirector;
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Update director: {}", director);
        Director updDirector = directorService.update(director);
        log.info("Director updated");
        return updDirector;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete director with id: {}", id);
        directorService.delete(id);
        log.info("Director deleted");
    }
}
