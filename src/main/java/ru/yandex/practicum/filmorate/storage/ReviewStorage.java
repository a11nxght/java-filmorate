package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review save(Review review);

    void delete(long id);

    Review update(Review review);

    Optional<Review> findById(long id);

    List<Review> findAll(Long filmId, int count);

    void addLike(Long reviewId, Long userId);

    void removeLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void removeDislike(Long reviewId, Long userId);
}
