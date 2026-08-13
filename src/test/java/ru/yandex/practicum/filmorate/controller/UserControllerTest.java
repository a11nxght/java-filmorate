//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.service.UserServiceImpl;
//import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
//import ru.yandex.practicum.filmorate.storage.UserStorage;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserControllerTest {
//
//    private UserController userController;
//
//    @BeforeEach
//    void setUpEach() {
//       UserStorage userStorage = new InMemoryUserStorage();
//       UserService userService = new UserServiceImpl(userStorage);
//       userController = new UserController(userService);
//    }
//
//    @Test
//    void add() {
//        addStartUser();
//        User user2 = new User();
//        user2.setId(10);
//        user2.setEmail("lolo@lol.com");
//        user2.setLogin("dimagordey");
//        user2.setBirthday(LocalDate.of(1993, 3, 5));
//        ValidationException validationException = assertThrows(ValidationException.class, () -> userController.add(user2));
//        assertEquals("Этот имейл уже используется", validationException.getMessage());
//        assertEquals(1, userController.getAll().size());
//    }
//
//    @Test
//    void update() {
//        addStartUser();
//        User user2 = new User();
//        user2.setId(10);
//        user2.setEmail("lolo@lol.com");
//        user2.setLogin("dimagordey");
//        user2.setBirthday(LocalDate.of(1993, 3, 5));
//        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userController.update(user2));
//        User updateUser = new User();
//        updateUser.setId(1);
//        updateUser.setEmail("updateLolo@lol.com");
//        updateUser.setLogin("Ivan Zolo");
//        updateUser.setBirthday(LocalDate.of(1993, 3, 5));
//        userController.update(updateUser);
//        assertEquals("Пользователь с таким id не найден.", notFoundException.getMessage());
//        assertEquals("updateLolo@lol.com", userController.getAll().stream().findFirst().get().getEmail());
//    }
//
//    private void addStartUser() {
//        User user = new User();
//        user.setEmail("lolo@lol.com");
//        user.setLogin("ivanzolo");
//        user.setBirthday(LocalDate.of(1999, 1, 2));
//        userController.add(user);
//    }
//}