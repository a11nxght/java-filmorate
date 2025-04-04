package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    public void validationTest() {
        UserController userController = new UserController();
        assertThrows(ValidationException.class, () -> {
            User user = new User(null, null, null, null, null);
            userController.create(user);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            User user = new User(null, "asdfadf@sadfas", null, null, null);
            userController.create(user);
        }, "Должно выводиться исключение валидации");
        assertThrows(ValidationException.class, () -> {
            User user = new User(null, "asdfadf@sadfas", "sdfas", null,
                    LocalDate.of(2030, 1, 1));
            userController.create(user);
        }, "Должно выводиться исключение валидации");
    }
}