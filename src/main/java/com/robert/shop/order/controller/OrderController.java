package com.robert.shop.order.controller;

import com.robert.shop.common.exception.ObjectNotFoundException;
import com.robert.shop.order.dto.InitOrder;
import com.robert.shop.order.dto.OrderDto;
import com.robert.shop.order.dto.OrderListDto;
import com.robert.shop.order.dto.OrderSummary;
import com.robert.shop.order.service.OrderService;
import com.robert.shop.order.service.PaymentService;
import com.robert.shop.order.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final PaymentService paymentService;

    @PostMapping
    public OrderSummary placeOrder(@RequestBody OrderDto orderDto, @AuthenticationPrincipal Long userId) {
        return orderService.placeOrder(orderDto, userId);
    }

    @GetMapping("/initData")
    public InitOrder initData() {
        return InitOrder.builder()
                .shipments(shipmentService.getShipments())
                .payments(paymentService.getPayments())
                .build();
    }

    @GetMapping
    public List<OrderListDto> getOrders(@AuthenticationPrincipal Long userId) {
        if (userId == null) {
            throw new ObjectNotFoundException("User is not logged in!");
        }
        return orderService.getOrdersForCustomer(userId);
    }

}
