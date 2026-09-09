package com.komal.carehub.service;

import com.komal.carehub.dto.ReviewDto;
import java.util.List;

public interface ReviewService {
    ReviewDto createReview(ReviewDto reviewDto);
    List<ReviewDto> getApprovedReviewsByProduct(Long productId);
    List<ReviewDto> getAllReviews();
    ReviewDto updateReviewStatus(Long id, String status);
}

