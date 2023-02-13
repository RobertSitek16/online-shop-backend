package com.robert.shop.admin.order.controller.dto;

import com.robert.shop.common.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminOrderDto {
    private Long id;
    private LocalDateTime placeDate;
    private OrderStatus orderStatus;
    private BigDecimal grossValue;
}
