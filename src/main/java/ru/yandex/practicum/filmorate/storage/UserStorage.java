package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User save(User user);

    void delete(long id);

    User update(User user);

    Optional<User> findById(long id);

    List<User> findAll();

    List<User> findFriends(long userId);

    List<User> findCommonFriends(long userId, long friendId);
}
