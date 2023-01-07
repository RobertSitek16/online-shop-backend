package com.robert.shop.cart.repository;

import com.robert.shop.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
