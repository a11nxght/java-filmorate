package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User add(User user);

    void delete(long id);

    User update(User user);

    Optional<User> get(long id);

    Collection<User> getAll();
}
