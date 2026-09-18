package com.komal.carehub.controller;

import com.komal.carehub.dto.ReviewDto;
import com.komal.carehub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Submit a new review (authenticated users).
     * The user's profile picture is automatically used as the review image.
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    /**
     * Get all reviews (admin use) — requires authentication.
     */
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /**
     * Get only approved reviews — public endpoint for the website testimonials page.
     */
    @GetMapping("/approved")
    public ResponseEntity<List<ReviewDto>> getApprovedReviews() {
        return ResponseEntity.ok(reviewService.getApprovedReviews());
    }

    /**
     * Get approved reviews for a specific product.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getApprovedReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsByProduct(productId));
    }

    /**
     * Update review status (admin moderation: APPROVED / REJECTED).
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ReviewDto> updateReviewStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(reviewService.updateReviewStatus(id, status));
    }
}
