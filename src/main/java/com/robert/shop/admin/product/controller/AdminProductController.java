package com.robert.shop.admin.product.controller;

import com.robert.shop.admin.product.dto.AdminProductDTO;
import com.robert.shop.admin.product.dto.UploadResponse;
import com.robert.shop.admin.product.model.AdminProduct;
import com.robert.shop.admin.product.service.AdminProductImageService;
import com.robert.shop.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.robert.shop.admin.common.utils.SlugifyUtils.slugifySlug;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    public static final Long EMPTY_ID = null;
    private final AdminProductService adminProductService;
    private final AdminProductImageService adminProductImageService;

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
    @Cacheable(cacheNames = "productBySlug", key = "#adminProductDTO.slug")
    public AdminProduct updateProduct(@Valid @RequestBody AdminProductDTO adminProductDTO, @PathVariable Long id) {
        return adminProductService.updateProduct(mapAdminProduct(adminProductDTO, id));
    }

    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }

    @PostMapping("admin/products/upload-image")
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String savedFileName = adminProductImageService.uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadResponse(savedFileName);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong!", e);
        }
    }

    @GetMapping("/data/productImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        Resource file = adminProductImageService.serveFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(file);
    }

    private AdminProduct mapAdminProduct(AdminProductDTO adminProductDTO, Long id) {
        return AdminProduct.builder()
                .id(id)
                .name(adminProductDTO.getName())
                .categoryId(adminProductDTO.getCategoryId())
                .description(adminProductDTO.getDescription())
                .fullDescription(adminProductDTO.getFullDescription())
                .price(adminProductDTO.getPrice())
                .salePrice(adminProductDTO.getSalePrice())
                .currency(adminProductDTO.getCurrency())
                .image(adminProductDTO.getImage())
                .slug(slugifySlug(adminProductDTO.getSlug()))
                .build();
    }

}
