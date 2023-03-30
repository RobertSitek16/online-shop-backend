package com.robert.shop.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductListDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String currency;
    private String image;
    private String slug;
}
