package com.robert.shop.admin.review.service;

import com.robert.shop.admin.review.model.AdminReview;
import com.robert.shop.admin.review.repository.AdminReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;

    public List<AdminReview> getReviews() {
        return adminReviewRepository.findAll();
    }

    @Transactional
    public void moderate(Long id) {
        adminReviewRepository.moderate(id);
    }

    public void delete(Long id) {
        adminReviewRepository.deleteById(id);
    }

}
