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
        if (Objects.isNull(user.getUser_friends())) {
            user.setUser_friends(new HashSet<>());
        }
        if (user.getUser_friends().contains(friendId)) {
            return;
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUser_friends())) {
            friend.setUser_friends(new HashSet<Long>());
        }

        user.getUser_friends().add(friendId);
        friend.getUser_friends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUser_friends())) {
            user.setUser_friends(new HashSet<>());
            return;
        }
        if (!user.getUser_friends().contains(friendId)) {
            throw new NotFoundException("Has error response");
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUser_friends())) {
            friend.setUser_friends(new HashSet<Long>());
        }
        user.getUser_friends().remove(friendId);
        friend.getUser_friends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUser_friends())) {
            return new ArrayList<>();
        }
        return user.getUser_friends().stream().map(userStorage::getUserById).toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        if (Objects.isNull(user.getUser_friends())) {
            return new ArrayList<>();
        }
        User friend = userStorage.getUserById(friendId);
        if (Objects.isNull(friend.getUser_friends())) {
            return new ArrayList<>();
        }
        return user.getUser_friends().stream()
                .filter(u -> friend.getUser_friends().contains(u))
                .map(userStorage::getUserById)
                .toList();
    }
}
