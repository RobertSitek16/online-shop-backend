package com.robert.shop.cart.service;

import com.robert.shop.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

}