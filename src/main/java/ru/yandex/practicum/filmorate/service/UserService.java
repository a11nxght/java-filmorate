package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getFriends())) {
            user.setFriends(new HashSet<>());
        }
        if (user.getFriends().contains(friendId)) {
            return;
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getFriends())) {
            friend.setFriends(new HashSet<Long>());
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getFriends())) {
            user.setFriends(new HashSet<>());
            return;
        }
        if (!user.getFriends().contains(friendId)) {
            return;
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getFriends())) {
            friend.setFriends(new HashSet<Long>());
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream().map(userStorage::getUserById).toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getFriends())) {
            return new ArrayList<>();
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getFriends())) {
            return new ArrayList<>();
        }
        return user.getFriends().stream()
                .filter(u -> friend.getFriends().contains(u))
                .map(userStorage::getUserById)
                .toList();
    }
}
