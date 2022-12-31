package com.robert.shop.category.dto;

import com.robert.shop.category.model.Category;
import com.robert.shop.product.dto.ProductListDto;
import org.springframework.data.domain.Page;

public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}
