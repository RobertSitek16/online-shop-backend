package com.robert.shop.admin.order.service;

import com.robert.shop.admin.order.model.AdminOrder;
import com.robert.shop.admin.order.repository.AdminOrderRepository;
import com.robert.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminExportService {

    private final AdminOrderRepository adminOrderRepository;

    public List<AdminOrder> exportOrders(LocalDateTime from, LocalDateTime to, OrderStatus orderStatus) {
        return adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(from, to, orderStatus);
    }
}
