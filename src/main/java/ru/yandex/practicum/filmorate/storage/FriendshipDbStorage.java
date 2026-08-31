package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_QUERY = """
            INSERT INTO friends (user_id, friend_id, status)
            VALUES (?, ?, ?);
            """;

    private static final String UPDATE_FRIENDSHIP_STATUS_QUERY = """
                    UPDATE friends
                    SET status = ?
                    WHERE user_id = ? AND friend_id = ?;
            """;

    private static final String DELETE_FRIENDSHIP_QUERY = """
                    DELETE FROM friends
                    WHERE user_id = ? AND friend_id = ?;
            """;

    private static final String SELECT_STATUS_QUERY = """
                    SELECT COUNT(*)
                    FROM friends
                    WHERE user_id = ? AND friend_id = ?;
            """;

    @Override
    public boolean addFriend(long userId, long friendId) {
        log.info("making a request to add a friendship");
        Integer isFriendAddUser = jdbcTemplate.queryForObject(SELECT_STATUS_QUERY, Integer.class,
                friendId, userId);
        int rowsUpdated;
        if (isFriendAddUser != null && isFriendAddUser > 0) {
            rowsUpdated = jdbcTemplate.update(INSERT_QUERY, userId, friendId, true);
            updateFriendshipStatus(true, friendId, userId);
        } else {
            rowsUpdated = jdbcTemplate.update(INSERT_QUERY, userId, friendId, false);
        }
        return rowsUpdated > 0;
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        log.info("making a request to remove a friendship");
        int rowsUpdated;
        rowsUpdated = jdbcTemplate.update(DELETE_FRIENDSHIP_QUERY, userId, friendId);
        updateFriendshipStatus(false, friendId, userId);
        return  rowsUpdated > 0;
    }

    public void updateFriendshipStatus(Boolean status, long userId, long friendId) {
        jdbcTemplate.update(UPDATE_FRIENDSHIP_STATUS_QUERY, status, userId, friendId);
    }
}
