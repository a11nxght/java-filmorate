package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ReviewDbStorage extends BaseRepository<Review> implements ReviewStorage {

    ReviewDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Review> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String INSERT_QUERY = """
            INSERT INTO reviews(content, is_positive, user_id, film_id)
            VALUES (?, ?, ?, ?);
            """;

    private static final String DELETE_QUERY = """
                    DELETE
                    FROM reviews
                    WHERE id = ?;
            """;

    private static final String UPDATE_QUERY = """
                    UPDATE reviews
                    SET content = ?,
                        is_positive = ?
                    WHERE id = ?;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM reviews
            WHERE id = ?;
            """;

    private static final String FIND_ALL_QUERY = """
                    SELECT *
                    FROM reviews
                    ORDER BY useful DESC
                    LIMIT ?;
            """;

    private static final String FIND_ALL_WITH_FILM_ID_QUERY = """
                    SELECT *
                    FROM reviews
                    WHERE film_id = ?
                    ORDER BY useful DESC
                    LIMIT ?;
            """;

    private static final String ADD_USEFUL_QUERY = """
            UPDATE reviews
            SET useful = useful + 1
            WHERE id = ?;
            """;

    private static final String REMOVE_USEFUL_QUERY = """
            UPDATE reviews
            SET useful = useful - 1
            WHERE id = ?;
            """;

    private static final String DELETE_USER_DISLIKE_QUERY = """
                    DELETE
                    FROM reviews_dislikes
                    WHERE reviews_id = ?
                      AND user_id = ?;
            """;

    private static final String DELETE_USER_LIKE_QUERY = """
                    DELETE
                    FROM reviews_likes
                    WHERE reviews_id = ?
                      AND user_id = ?;
            """;

    private static final String INSERT_USER_LIKE_QUERY = """
                    INSERT
                    INTO reviews_likes(reviews_id, user_id)
                    VALUES (?, ?);
            """;

    private static final String INSERT_USER_DISLIKE_QUERY = """
                    INSERT
                    INTO reviews_dislikes(reviews_id, user_id)
                    VALUES (?, ?);
            """;

    @Override
    public Review save(Review review) {
        log.info("Making request to save review {} in DB", review);
        long id = save(INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());
        review.setReviewId(id);
        return review;
    }

    @Override
    public void delete(long id) {
        log.info("Making request to delete review {} from DB", id);
        delete(DELETE_QUERY, id);
    }

    @Override
    public Review update(Review review) {
        log.info("Making request to update review {} in DB", review);
        update(UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return review;
    }

    @Override
    public Optional<Review> findById(long id) {
        log.info("Making request to find review with id: {} in DB", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Review> findAll(Long filmId, int count) {
        log.info("Making request to find reviews with filmId: {} in DB", filmId);
        if (filmId != null) {
            return findMany(FIND_ALL_WITH_FILM_ID_QUERY, filmId, count);
        } else {
            return findMany(FIND_ALL_QUERY, count);
        }
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        log.info("Making request to add like to review {} in DB", reviewId);
        boolean isDelete = delete(DELETE_USER_DISLIKE_QUERY, reviewId, userId);
        if (isDelete) {
            update(ADD_USEFUL_QUERY, reviewId);
        }
        update(INSERT_USER_LIKE_QUERY, reviewId, userId);
        update(ADD_USEFUL_QUERY, reviewId);
    }

    @Override
    public void removeLike(Long reviewId, Long userId) {
        log.info("Making request to remove like to review {} in DB", reviewId);
        delete(DELETE_USER_LIKE_QUERY, reviewId, userId);
        update(REMOVE_USEFUL_QUERY, reviewId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        log.info("Making request to add dislike to review {} in DB", reviewId);
        boolean isDelete = delete(DELETE_USER_LIKE_QUERY, reviewId, userId);
        if (isDelete) {
            update(REMOVE_USEFUL_QUERY, reviewId);
        }
        update(INSERT_USER_DISLIKE_QUERY, reviewId, userId);
        update(REMOVE_USEFUL_QUERY, reviewId);
    }

    @Override
    public void removeDislike(Long reviewId, Long userId) {
        log.info("Making request to remove dislike to review {} in DB", reviewId);
        delete(DELETE_USER_DISLIKE_QUERY, reviewId, userId);
        update(ADD_USEFUL_QUERY, reviewId);
    }
}
