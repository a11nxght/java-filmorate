package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository()
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO likes(film_id, user_id)
            VALUES(?,?);
            """;

    private static final String REMOVE_LIKE_QUERY = """
            DELETE
            FROM likes
            WHERE film_id = ?
              AND user_id = ?;
    """;

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        jdbcTemplate.update(REMOVE_LIKE_QUERY, filmId, userId);
    }
}
