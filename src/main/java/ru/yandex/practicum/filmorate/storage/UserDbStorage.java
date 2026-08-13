package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository("UserDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage{

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
            FROM users u
            JOIN friends f  ON u.id = f.user_id
            WHERE f.friend_id = ?;
            """;

    private static final String FIND_COMMON_FRIENDS_QUERY = """
            SELECT *
            FROM users
            WHERE id IN
                (SELECT u.id
                 FROM users u
                 JOIN friends f ON u.id = f.friend_id
                 WHERE f.user_id = ?)
              AND id IN
                (SELECT u.id
                 FROM users u
                 JOIN friends f ON u.id = f.user_id
                 WHERE f.friend_id = ?);
    """;

    public UserDbStorage(final JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public User save(User user) {
        long id = save(INSERT_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public void delete(long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<User> findFriends(long userId) {
        return findMany(FIND_FRIENDS_QUERY, userId);
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, friendId);
    }

}
