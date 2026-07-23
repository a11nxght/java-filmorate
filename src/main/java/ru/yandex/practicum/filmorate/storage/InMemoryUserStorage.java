package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Added user: {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("Updated user: {}", user);
        return user;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    private long getNextId() {
        return users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0)
                + 1;
    }
}
