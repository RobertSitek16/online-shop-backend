package com.robert.shop.cart.controller;

import com.robert.shop.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItems")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
    
    @GetMapping("/count/{cartId}")
    public Long countItemsInCart(@PathVariable Long cartId) {
        return cartItemService.countItemsInCart(cartId);
    }

}
