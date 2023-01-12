package com.robert.shop.order.repository;

import com.robert.shop.order.model.OrderRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRowRepository extends JpaRepository<OrderRow, Long> {
}
