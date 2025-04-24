package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (user.getUserFriends().contains(friendId)) {
            return;
        }
        User friend = userStorage.getUserById(friendId);
        user.getUserFriends().add(friendId);
        friend.getUserFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (user.getUserFriends().isEmpty()) {
            return;
        }
        if (!user.getUserFriends().contains(friendId)) {
            throw new NotFoundException("Has error response");
        }
        User friend = userStorage.getUserById(friendId);
        user.getUserFriends().remove(friendId);
        friend.getUserFriends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        return user.getUserFriends().stream().map(userStorage::getUserById).toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        return user.getUserFriends().stream()
                .filter(u -> friend.getUserFriends().contains(u))
                .map(userStorage::getUserById)
                .toList();
    }
}
