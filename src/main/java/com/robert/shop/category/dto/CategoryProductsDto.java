package com.robert.shop.category.dto;

import com.robert.shop.common.model.Category;
import com.robert.shop.common.dto.ProductListDto;
import org.springframework.data.domain.Page;

public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}
