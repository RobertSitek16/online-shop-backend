package com.robert.shop.category.controller;

import com.robert.shop.category.dto.CategoryProductsDto;
import com.robert.shop.common.model.Category;
import com.robert.shop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Cacheable("categories")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{slug}/products")
    @Cacheable("categoriesWithProducts")
    public CategoryProductsDto getCategoryWithProducts(@PathVariable
                                                       @Pattern(regexp = "[a-z0-9\\-]+")
                                                       @Length(max = 255) String slug,
                                                       Pageable pageable) {
        return categoryService.getCategoryWithProducts(slug, pageable);
    }


}
