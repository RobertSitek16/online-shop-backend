package com.robert.shop.admin.product.controller;

import com.robert.shop.admin.product.dto.AdminProductDTO;
import com.robert.shop.admin.product.model.AdminProduct;
import com.robert.shop.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    public static final Long EMPTY_ID = null;
    private final AdminProductService adminProductService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) {
        return adminProductService.getProducts(pageable);
    }

    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return adminProductService.getProduct(id);
    }

    @PostMapping("/admin/products")
    public AdminProduct createProduct(@Valid @RequestBody AdminProductDTO adminProductDTO) {
        return adminProductService.createProduct(mapAdminProduct(adminProductDTO, EMPTY_ID));
    }

    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@Valid @RequestBody AdminProductDTO adminProductDTO, @PathVariable Long id) {
        return adminProductService.updateProduct(mapAdminProduct(adminProductDTO, id));
    }

    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }

    private AdminProduct mapAdminProduct(AdminProductDTO adminProductDTO, Long id) {
        return AdminProduct.builder()
                .id(id)
                .name(adminProductDTO.getName())
                .category(adminProductDTO.getCategory())
                .description(adminProductDTO.getDescription())
                .price(adminProductDTO.getPrice())
                .currency(adminProductDTO.getCurrency())
                .build();
    }

}
