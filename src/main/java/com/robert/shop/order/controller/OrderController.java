package com.robert.shop.order.controller;

import com.robert.shop.common.exception.ObjectNotFoundException;
import com.robert.shop.common.model.OrderStatus;
import com.robert.shop.order.dto.InitOrder;
import com.robert.shop.order.dto.NotificationDto;
import com.robert.shop.order.dto.NotificationReceiveDto;
import com.robert.shop.order.dto.OrderDto;
import com.robert.shop.order.dto.OrderListDto;
import com.robert.shop.order.dto.OrderSummary;
import com.robert.shop.order.model.Order;
import com.robert.shop.order.service.OrderService;
import com.robert.shop.order.service.PaymentService;
import com.robert.shop.order.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
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

    @GetMapping("/notification/{orderHash}")
    public NotificationDto notificationShow(@PathVariable @Length(max = 12) String orderHash) {
        Order order = orderService.getOrderByOrderHash(orderHash);
        return new NotificationDto(order.getOrderStatus() == OrderStatus.PAID);
    }

    @PostMapping("/notification/{orderHash}")
    public void notificationReceive(@PathVariable @Length(max = 12) String orderHash,
                                    @RequestBody NotificationReceiveDto receiveDto,
                                    HttpServletRequest request) {
        String forwardedAddress = request.getHeader("x-forwarded-for");
        orderService.receiveNotification(
                orderHash,
                receiveDto,
                StringUtils.isNotEmpty(forwardedAddress) ? forwardedAddress : request.getRemoteAddr()
        );
    }

}
