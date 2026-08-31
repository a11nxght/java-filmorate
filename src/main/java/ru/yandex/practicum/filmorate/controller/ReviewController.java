package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("Adding review: {}", review);
        return reviewService.add(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        log.info("Updating review: {}", review);
        return reviewService.update(review);
    }

    @DeleteMapping("{id}")
    public void deleteReview(@PathVariable Long id) {
        log.info("Deleting review: {}", id);
        reviewService.delete(id);
    }

    @GetMapping("{id}")
    public Review getReview(@PathVariable Long id) {
        log.info("Getting review: {}", id);
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(defaultValue = "10") int count) {
        log.info("Getting reviews with filmId: {}, count: {}", filmId, count);
        return reviewService.findAll(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLikeReview(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.info("like review id {} userId {}", id, userId);
        reviewService.addLike(id, userId);
    }


    @PutMapping("{id}/dislike/{userId}")
    public void addDislikeReview(@PathVariable Long id,
                                @PathVariable Long userId) {
        log.info("dislike review id {} userId {}", id, userId);
        reviewService.addDislike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable Long id,
                                    @PathVariable Long userId) {
        log.info("delete like review id {} userId {}", id, userId);
        reviewService.removeLike(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable Long id,
                                      @PathVariable Long userId) {
        log.info("delete dislike review id {} userId {}", id, userId);
        reviewService.removeDislike(id, userId);
    }

}
