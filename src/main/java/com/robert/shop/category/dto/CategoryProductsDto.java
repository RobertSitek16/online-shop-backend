package com.robert.shop.category.dto;

import com.robert.shop.category.model.Category;
import com.robert.shop.product.model.Product;
import org.springframework.data.domain.Page;

public record CategoryProductsDto(Category category, Page<Product> products) {
}
