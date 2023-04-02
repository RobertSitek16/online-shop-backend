package com.robert.shop.homepage.dto;

import com.robert.shop.common.model.Product;

import java.util.List;

public record HomePageDto(List<Product> saleProducts) {
}
