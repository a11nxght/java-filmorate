package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void  setUpEach() {
        userController = new UserController();
    }

    @Test
    void add() {
        addStartUser();
        User user2 = User.builder()
                .email("lolo@lol.com")
                .login("dimagordey")
                .birthday(LocalDate.of(1993, 3, 5))
                .build();
        ValidationException validationException = assertThrows(ValidationException.class, () -> userController.add(user2));
        assertEquals("Этот имейл уже используется", validationException.getMessage());
        assertEquals(1, userController.getAll().size());
    }

    @Test
    void update() {
        addStartUser();
        User user2 = User.builder()
                .id(10)
                .email("lolo@lol.com")
                .login("dimagordey")
                .birthday(LocalDate.of(1993, 3, 5))
                .build();
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userController.update(user2));
        User updateUser = User.builder()
                .id(1)
                .email("updateLolo@lol.com")
                .login("Ivan Zolo")
                .birthday(LocalDate.of(1993, 3, 5))
                .build();
        userController.update(updateUser);
        assertEquals("Пользователь с таким id не найден.", notFoundException.getMessage());
        assertEquals("updateLolo@lol.com", userController.getAll().stream().findFirst().get().getEmail());
    }

    private void addStartUser() {
        User user = User.builder()
                .email("lolo@lol.com")
                .login("ivanzolo")
                .birthday(LocalDate.of(1999, 1, 2))
                .build();
        userController.add(user);
    }
}