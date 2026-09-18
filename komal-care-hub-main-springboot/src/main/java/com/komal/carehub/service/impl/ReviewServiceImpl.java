package com.komal.carehub.service.impl;

import com.komal.carehub.dto.EntityMapper;
import com.komal.carehub.dto.ReviewDto;
import com.komal.carehub.entity.Product;
import com.komal.carehub.entity.Review;
import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.ReviewStatus;
import com.komal.carehub.repository.ProductRepository;
import com.komal.carehub.repository.ReviewRepository;
import com.komal.carehub.repository.UserRepository;
import com.komal.carehub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reviewDto.getUserId()));

        Review review = Review.builder()
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .userImage(user.getProfilePicture())
                .status(ReviewStatus.PENDING)
                .user(user)
                .build();

        // If productId is provided, attach the product
        if (reviewDto.getProductId() != null) {
            Product product = productRepository.findById(reviewDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + reviewDto.getProductId()));
            review.setProduct(product);
        }

        Review savedReview = reviewRepository.save(review);
        return entityMapper.toReviewDto(savedReview);
    }

    @Override
    public List<ReviewDto> getApprovedReviewsByProduct(Long productId) {
        return reviewRepository.findByProductIdAndStatus(productId, ReviewStatus.APPROVED)
                .stream()
                .map(entityMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getApprovedReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED)
                .stream()
                .map(entityMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(entityMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDto updateReviewStatus(Long id, String status) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        review.setStatus(ReviewStatus.valueOf(status.toUpperCase()));
        Review updatedReview = reviewRepository.save(review);
        return entityMapper.toReviewDto(updatedReview);
    }
}

