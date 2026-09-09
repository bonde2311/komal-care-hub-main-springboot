package com.komal.carehub.repository;

import com.komal.carehub.entity.Review;
import com.komal.carehub.entity.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStatus(ReviewStatus status);
    List<Review> findByProductIdAndStatus(Long productId, ReviewStatus status);
}

