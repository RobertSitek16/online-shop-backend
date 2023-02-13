package com.robert.shop.order.service.mapper;

import com.robert.shop.order.dto.OrderListDto;
import com.robert.shop.order.model.Order;

import java.util.List;

public class OrderDtoMapper {

    public static List<OrderListDto> mapToOrderListDto(List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderListDto(
                        order.getId(),
                        order.getPlaceDate(),
                        order.getOrderStatus().name(),
                        order.getGrossValue()
                )).toList();
    }

}
