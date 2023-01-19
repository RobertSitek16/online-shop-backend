package com.robert.shop.order.dto;

import com.robert.shop.order.model.Payment;
import com.robert.shop.order.model.Shipment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InitOrder {
    private List<Shipment> shipments;
    private List<Payment> payments;
}
