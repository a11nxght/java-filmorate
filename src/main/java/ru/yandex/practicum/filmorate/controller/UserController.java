package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Поступил запрос на создание пользователя");
        User createdUser = userStorage.create(user);
        log.info("Cоздан пользователь с ID: {}", user.getId());
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("поступил запрос на обновление пользователя");
        User updatedUser = userStorage.update(newUser);
        log.info("Обновлен пользователь с ID: {}", updatedUser.getId());
        return updatedUser;
    }
}
