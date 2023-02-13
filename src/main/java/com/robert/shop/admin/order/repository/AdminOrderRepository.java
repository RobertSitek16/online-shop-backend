package com.robert.shop.admin.order.repository;

import com.robert.shop.admin.order.model.AdminOrder;
import com.robert.shop.common.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {

    List<AdminOrder> findAllByPlaceDateIsBetweenAndOrderStatus(LocalDateTime from, LocalDateTime to, OrderStatus orderStatus);

}
