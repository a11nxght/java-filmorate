package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("Adding user {}", user);
        User result = userService.add(user);
        log.info("User added with id {}", result.getId());
        return result;
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        log.info("Updating user {}", updateUser);
        userService.update(updateUser);
        log.info("User updated");
        return updateUser;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Getting all users");
        return userService.findAll();
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Adding friend {}", friendId);
        userService.addFriend(id, friendId);
        log.info("Friend added");
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Deleting friend {}", friendId);
        userService.removeFriend(id, friendId);
        log.info("Friend deleted");
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Getting friends for user(id:{})", id);
        List<User> result = userService.findFriends(id);
        log.info("Friends found for user(id:{})", id);
        return result;
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Getting common friends for user(id:{})", id);
        List<User> result = userService.findCommonFriends(id, otherId);
        log.info("Common friends found for user(id:{})", id);
        return result;
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Deleting user {}", userId);
        userService.delete(userId);
        log.info("User deleted");
    }
}
