package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User update(User newUser);

    User delete(User user);

    Collection<User> getAll();

    User getUserById(Long id);
}
