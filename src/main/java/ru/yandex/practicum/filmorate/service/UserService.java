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
        if (Objects.isNull(user.getUserFriends())) {
            user.setUserFriends(new HashSet<>());
        }
        if (user.getUserFriends().contains(friendId)) {
            return;
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUserFriends())) {
            friend.setUserFriends(new HashSet<Long>());
        }

        user.getUserFriends().add(friendId);
        friend.getUserFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUserFriends())) {
            user.setUserFriends(new HashSet<>());
            return;
        }
        if (!user.getUserFriends().contains(friendId)) {
            throw new NotFoundException("Has error response");
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUserFriends())) {
            friend.setUserFriends(new HashSet<Long>());
        }
        user.getUserFriends().remove(friendId);
        friend.getUserFriends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUserFriends())) {
            return new ArrayList<>();
        }
        return user.getUserFriends().stream().map(userStorage::getUserById).toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUserFriends())) {
            return new ArrayList<>();
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUserFriends())) {
            return new ArrayList<>();
        }
        return user.getUserFriends().stream()
                .filter(u -> friend.getUserFriends().contains(u))
                .map(userStorage::getUserById)
                .toList();
    }
}
