package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    public void validationTest() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        UserService userService = new UserService(inMemoryUserStorage);
        UserController userController = new UserController(inMemoryUserStorage, userService);
        assertThrows(ValidationException.class, () -> {
            User user = new User();
            userController.create(user);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("asdfadf@sadfas");
            userController.create(user);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("asdfadf@sadfas");
            user.setLogin("sdfas");
            user.setBirthday(LocalDate.of(2030, 1, 1));
            userController.create(user);
        }, "Должно выводиться исключение валидации");
    }
}