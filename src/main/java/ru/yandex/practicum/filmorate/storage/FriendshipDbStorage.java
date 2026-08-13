package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage implements  FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_QUERY = """
            INSERT INTO friends (user_id, friend_id, status)
            VALUES (?, ?, ?);
            """;

    private static final String IS_FRIENDS_QUERY = """
            SELECT status
            FROM friends
            WHERE user_id = ? and friend_id = ?;
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

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(INSERT_QUERY, userId, friendId, true);
        jdbcTemplate.update(INSERT_QUERY, friendId, userId, true);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_FRIENDSHIP_QUERY, userId, friendId);
        jdbcTemplate.update(DELETE_FRIENDSHIP_QUERY, friendId, userId);
    }

    public void updateFriendshipStatus(Boolean status, long userId, long friendId) {
        jdbcTemplate.update(UPDATE_FRIENDSHIP_STATUS_QUERY, status, userId, friendId);
    }
}
