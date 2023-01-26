package com.robert.shop.admin.order.controller.dto;

import com.robert.shop.admin.order.model.AdminOrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminOrderDto {
    private Long id;
    private LocalDateTime placeDate;
    private AdminOrderStatus orderStatus;
    private BigDecimal grossValue;
}
