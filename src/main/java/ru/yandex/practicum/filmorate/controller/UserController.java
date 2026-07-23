package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        return userService.update(updateUser);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.findAll();
    }
}
