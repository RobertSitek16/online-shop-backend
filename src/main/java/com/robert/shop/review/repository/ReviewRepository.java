package com.robert.shop.review.repository;

import com.robert.shop.common.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
