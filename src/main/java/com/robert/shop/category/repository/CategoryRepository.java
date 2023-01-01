package com.robert.shop.category.repository;

import com.robert.shop.common.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findBySlug(String slug);
}
