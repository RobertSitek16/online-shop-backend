package com.robert.shop.admin.product.service;

import com.robert.shop.admin.product.model.AdminProduct;
import com.robert.shop.admin.product.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;

    public Page<AdminProduct> getProducts(Pageable pageable) {
        return adminProductRepository.findAll(pageable);
    }

    public AdminProduct getProduct(Long id) {
        return adminProductRepository.findById(id).orElseThrow();
    }

    public AdminProduct createProduct(AdminProduct adminProduct) {
        return adminProductRepository.save(adminProduct);
    }

    public AdminProduct updateProduct(AdminProduct adminProduct) {
        return adminProductRepository.save(adminProduct);
    }

    public void deleteProduct(Long id) {
        adminProductRepository.deleteById(id);
    }
}
