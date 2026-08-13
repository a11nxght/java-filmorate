package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public UserServiceImpl(@Qualifier("UserDbStorage") UserStorage userStorage,
                           FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        try {
            return userStorage.save(user);
        } catch (DataIntegrityViolationException e) {
            log.debug("Email или Login уже используются: {}", user.getEmail());
            throw new ValidationException("Этот имейл или логин уже используется");
        }
    }

    @Override
    public User update(User updateUser) {
        userStorage.findById(updateUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + updateUser.getId() + " не найден."));
        return userStorage.update(updateUser);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }

    @Override
    public User findById(long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll().stream().toList();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));

        friendshipStorage.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));
        friendshipStorage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> findFriends(long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        return userStorage.findFriends(userId);
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));
        return userStorage.findCommonFriends(userId, friendId);
    }
}
