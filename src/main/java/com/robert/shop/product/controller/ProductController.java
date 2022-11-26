package com.robert.shop.product.controller;

import com.robert.shop.product.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProduct() {
        return List.of(
                new Product("Product 1", "Category 1", "Description 1", new BigDecimal("11.99"), "EUR"),
                new Product("Product 2", "Category 2", "Description 2", new BigDecimal("12.99"), "EUR"),
                new Product("Product 3", "Category 3", "Description 3", new BigDecimal("13.99"), "EUR"),
                new Product("Product 4", "Category 4", "Description 4", new BigDecimal("14.99"), "EUR")
        );
    }
}
