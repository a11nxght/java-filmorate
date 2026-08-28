package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.event_feed.Event;
import ru.yandex.practicum.filmorate.model.event_feed.EventType;
import ru.yandex.practicum.filmorate.model.event_feed.Operation;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Override
    public Review add(Review review) {
        log.info("Adding review {}", review);
        if (review.getFilmId() == null || review.getUserId() == null || review.getIsPositive() == null) {
            throw new InternalServerException("FilmId and UserId are mandatory");
        }
        filmStorage.findById(review.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film with id " + review.getFilmId() + " not found"));
        userStorage.findById(review.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + review.getUserId() + " not found"));
        Review savedReview = reviewStorage.save(review);
        eventStorage.addEvent(Event.builder()
                .userId(review.getUserId())
                .eventType(EventType.REVIEW)
                .operation(Operation.ADD)
                .entityId(savedReview.getReviewId()).build());
        return savedReview;
    }

    @Override
    public Review update(Review review) {
        log.info("Updating review {}", review);
        Review updReview = reviewStorage.update(review);
        eventStorage.addEvent(Event.builder()
                .userId(review.getUserId())
                .eventType(EventType.REVIEW)
                .operation(Operation.UPDATE)
                .entityId(updReview.getReviewId()).build());
        return updReview;
    }

    @Override
    public void delete(long id) {
        log.info("Deleting review {}", id);
        Long userId = reviewStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id " + id + " not found."))
                .getUserId();
        reviewStorage.delete(id);
        eventStorage.addEvent(Event.builder()
                .userId(userId)
                .eventType(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .entityId(id).build());
    }

    @Override
    public Review findById(long id) {
        log.info("Finding review {}", id);
        return reviewStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id " + id + " not found"));
    }

    @Override
    public List<Review> findAll(Long filmId, int count) {
        if (filmId == null) {
            return reviewStorage.findAll(null, count);
        } else {
            filmStorage.findById(filmId)
                    .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
            return reviewStorage.findAll(filmId, count);
        }
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        log.info("Adding like review {}", reviewId);
        reviewStorage.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id " + reviewId + " not found"));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        reviewStorage.addLike(reviewId, userId);
    }

    @Override
    public void removeLike(Long reviewId, Long userId) {
        log.info("Removing like review {}", reviewId);
        reviewStorage.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id " + reviewId + " not found"));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        reviewStorage.removeLike(reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        log.info("Adding dislike review {}", reviewId);
        reviewStorage.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id " + reviewId + " not found"));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        reviewStorage.addDislike(reviewId, userId);
    }

    @Override
    public void removeDislike(Long reviewId, Long userId) {
        log.info("Removing dislike review {}", reviewId);
        reviewStorage.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id " + reviewId + " not found"));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        reviewStorage.removeDislike(reviewId, userId);
    }
}
