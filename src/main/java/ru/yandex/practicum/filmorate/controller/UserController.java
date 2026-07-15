package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.debug("Email {} is already in use", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Added user: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        if (users.containsKey(updateUser.getId())) {
            if (updateUser.getName() == null || updateUser.getName().isBlank()) {
                if (users.get(updateUser.getId()).getName() != null && !users.get(updateUser.getId()).getName().isBlank()) {
                    updateUser.setName(users.get(updateUser.getId()).getName());
                } else {
                    updateUser.setName(updateUser.getLogin());
                }
            }
            users.put(updateUser.getId(), updateUser);
            log.info("Updated user: {}", updateUser);
            return updateUser;
        }
        log.debug("User not found: {}", updateUser);
        throw new NotFoundException("Пользователь с таким id не найден.");
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    private long getNextId() {
        return users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0)
                + 1;
    }
}
