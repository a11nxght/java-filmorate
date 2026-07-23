package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user);

    void delete(long id);

    User findById(long id);

    List<User> findAll();

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> findFriends(long userId);

    List<User> findCommonFriends(long userId, long friendId);
}
