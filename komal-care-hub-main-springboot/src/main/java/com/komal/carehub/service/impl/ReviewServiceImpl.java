package com.komal.carehub.service.impl;

import com.komal.carehub.dto.ReviewDto;
import com.komal.carehub.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        return null;
    }

    @Override
    public List<ReviewDto> getApprovedReviewsByProduct(Long productId) {
        return null;
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return null;
    }

    @Override
    public ReviewDto updateReviewStatus(Long id, String status) {
        return null;
    }
}

