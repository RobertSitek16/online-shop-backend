package com.robert.shop.admin.product.repository;

import com.robert.shop.admin.product.model.AdminProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<AdminProduct, Long> {
}
