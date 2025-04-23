package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Поступил запрос на вывод всех пользователей");
        return userStorage.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Поступил запрос на создание пользователя");
        User createdUser = userStorage.create(user);
        log.info("Cоздан пользователь с ID: {}", user.getId());
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Поступил запрос на обновление пользователя");
        User updatedUser = userStorage.update(newUser);
        log.info("Обновлен пользователь с ID: {}", updatedUser.getId());
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Поступил запрос на вывод пользователя с id: {}", id);
        return userStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Поступил запрос пользователя с ID: {} на добавление друга с ID: {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Поступил запрос пользователя с ID: {} на удаление другас ID: {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        log.info("Поступил запрос на вывод друзей пользователя с ID: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Поступил запрос на вывод общих друзей у пользователей с ID: {}, {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
