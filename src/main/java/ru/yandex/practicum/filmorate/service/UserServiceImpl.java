package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User add(User user) {
        if (userStorage.getAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.debug("Email {} is already in use", user.getEmail());
            throw new ValidationException("Этот имейл уже используется");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    @Override
    public User update(User updateUser) {
        userStorage.get(updateUser.getId()).orElseThrow(() -> {
            log.debug("User not found with id {}", updateUser.getId());
            return new NotFoundException("Пользователь с таким id не найден.");
        });
        return userStorage.update(updateUser);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }

    @Override
    public User findById(long id) {
        return userStorage.get(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
    }

    @Override
    public List<User> findAll() {
        return userStorage.getAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
    }
}
