package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("UserDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_QUERY = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?, ?, ?, ?);
            """;

    private static final String DELETE_QUERY = """
                    DELETE FROM users WHERE id = ?;
            """;

    private static final String UPDATE_QUERY = """
                    UPDATE users
                    SET email = ?,
                        login = ?,
                        name = ?,
                        birthday = ?
                    WHERE id = ?;
            """;

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM users WHERE id = ?;
            """;

    private static final String FIND_ALL_QUERY = """
                    SELECT * FROM users;
            """;

    private static final String FIND_FRIENDS_QUERY = """
            SELECT *
            FROM users
            WHERE id IN
                (SELECT friend_id
                 FROM friends
                 WHERE user_id = ?);
            """;

    private static final String FIND_COMMON_FRIENDS_QUERY = """
                    SELECT *
                    FROM users
                    WHERE id IN
                        (SELECT friend_id
                         FROM friends
                         WHERE user_id = ?)
                      AND id IN
                        (SELECT friend_id
                         FROM friends
                         WHERE user_id = ?);
            """;

    public UserDbStorage(final JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public User save(User user) {
        log.info("making a request to save user");
        long id = save(INSERT_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public void delete(long id) {
        log.info("making a request to delete user");
        delete(DELETE_QUERY, id);
    }

    @Override
    public User update(User user) {
        log.info("making a request to update user");
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        log.info("making a request to find user by id");
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<User> findAll() {
        log.info("making a request to find all users");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<User> findFriends(long userId) {
        log.info("making a request to find friends by user id");
        return findMany(FIND_FRIENDS_QUERY, userId);
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        log.info("making a request to find common friends by user id");
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, friendId);
    }

}
