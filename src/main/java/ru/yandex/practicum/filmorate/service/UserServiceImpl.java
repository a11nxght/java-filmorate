package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.event_feed.Event;
import ru.yandex.practicum.filmorate.model.event_feed.EventType;
import ru.yandex.practicum.filmorate.model.event_feed.Operation;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;

    @Override
    public User add(User user) {
        log.info("Start adding user {}", user);
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
        log.info("Start updating user {}", updateUser);
        userStorage.findById(updateUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + updateUser.getId() + " не найден."));
        return userStorage.update(updateUser);
    }

    @Override
    public void delete(long id) {
        log.info("Start deleting user {}", id);
        userStorage.delete(id);
    }

    @Override
    public User findById(long id) {
        log.info("Start finding user {}", id);
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден."));
    }

    @Override
    public List<User> findAll() {
        log.info("Start finding all users");
        return userStorage.findAll().stream().toList();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.info("Start adding friend {} to the user {}", friendId, userId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));

        boolean isAdded = friendshipStorage.addFriend(userId, friendId);
        if (isAdded) {
            eventStorage.addEvent(Event.builder()
                    .userId(userId)
                    .eventType(EventType.FRIEND)
                    .operation(Operation.ADD)
                    .entityId(friendId)
                    .build());
        }
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        log.info("Start removing friend {} to the user {}", friendId, userId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));
        boolean isDeleted = friendshipStorage.removeFriend(userId, friendId);
        if (isDeleted) {
            eventStorage.addEvent(Event.builder()
                    .userId(userId)
                    .eventType(EventType.FRIEND)
                    .operation(Operation.REMOVE)
                    .entityId(friendId)
                    .build());
        }
    }

    @Override
    public List<User> findFriends(long userId) {
        log.info("Start finding friends for user {}", userId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        return userStorage.findFriends(userId);
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        log.info("Start finding common friends for user {} and friend {}", userId, friendId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не найден."));
        return userStorage.findCommonFriends(userId, friendId);
    }

    @Override
    public List<Film> findRecommendations(long userId) {
        log.info("Start finding recommendations for user {}", userId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        return filmStorage.findRecommendations(userId);
    }

    @Override
    public List<Event> findEvents(long userId) {
        log.info("Start finding events for user {}", userId);
        return eventStorage.findUserEvents(userId);
    }
}
