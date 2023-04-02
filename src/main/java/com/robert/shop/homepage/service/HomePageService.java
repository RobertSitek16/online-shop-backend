package com.robert.shop.homepage.service;

import com.robert.shop.common.model.Product;
import com.robert.shop.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final ProductRepository productRepository;

    public List<Product> getSaleProducts() {
        return productRepository.findTop10BySalePriceIsNotNull();
    }
}
