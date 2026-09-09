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

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getApprovedReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsByProduct(productId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReviewDto> updateReviewStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(reviewService.updateReviewStatus(id, status));
    }
}

