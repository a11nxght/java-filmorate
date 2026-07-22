package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Added user: {}", user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if  (!users.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
        users.put(user.getId(), user);
        log.info("Updated user: {}", user);
        return user;
    }

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    private long getNextId() {
        return users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0)
                + 1;
    }
}
