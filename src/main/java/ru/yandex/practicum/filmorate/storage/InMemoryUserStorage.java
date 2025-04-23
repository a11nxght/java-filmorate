package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Нет пользователя с id %d", id));
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        isUserValid(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("ID пользователя должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            isUserValid(newUser);
            User oldUser = users.get(newUser.getId());

            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Обновлен пользователь с ID: {}", oldUser.getId());
            return oldUser;
        }
        log.warn("пользователь c ID: {} не найден", newUser.getId());
        throw new NotFoundException("пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public User delete(User user) {
        return null;
    }

    private void isUserValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Has error response");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Has error response");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("вместо имени будет использоваться логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("Has error response");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }


}
