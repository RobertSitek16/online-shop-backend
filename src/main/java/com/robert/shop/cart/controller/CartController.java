package com.robert.shop.cart.controller;

import com.robert.shop.cart.dto.CartProductDto;
import com.robert.shop.cart.dto.CartSummaryDto;
import com.robert.shop.cart.mapper.CartMapper;
import com.robert.shop.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public CartSummaryDto getCart(@PathVariable Long id) {
        return CartMapper.mapToCartSummary(cartService.getCart(id));
    }

    @PutMapping("/{id}")
    public CartSummaryDto addProductToCart(@PathVariable Long id, @RequestBody CartProductDto cartProductDto) {
        return CartMapper.mapToCartSummary(cartService.addProductToCart(id, cartProductDto));
    }

    @PutMapping("/{id}/update")
    public CartSummaryDto updateCart(@PathVariable Long id, @RequestBody List<CartProductDto> cartProductDtoList) {
        return CartMapper.mapToCartSummary(cartService.updateCart(id, cartProductDtoList));
    }

}
