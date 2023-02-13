package com.robert.shop.order.service;

import com.robert.shop.common.mail.EmailClientService;
import com.robert.shop.common.model.Cart;
import com.robert.shop.common.repository.CartItemRepository;
import com.robert.shop.common.repository.CartRepository;
import com.robert.shop.order.dto.OrderDto;
import com.robert.shop.order.dto.OrderListDto;
import com.robert.shop.order.dto.OrderSummary;
import com.robert.shop.order.model.Order;
import com.robert.shop.order.model.OrderRow;
import com.robert.shop.order.model.Payment;
import com.robert.shop.order.model.Shipment;
import com.robert.shop.order.repository.OrderRepository;
import com.robert.shop.order.repository.OrderRowRepository;
import com.robert.shop.order.repository.PaymentRepository;
import com.robert.shop.order.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.robert.shop.order.service.mapper.OrderDtoMapper.mapToOrderListDto;
import static com.robert.shop.order.service.mapper.OrderEmailMessageMapper.createEmailMessage;
import static com.robert.shop.order.service.mapper.OrderMapper.createNewOrder;
import static com.robert.shop.order.service.mapper.OrderMapper.createOrderSummary;
import static com.robert.shop.order.service.mapper.OrderMapper.mapToOrderRow;
import static com.robert.shop.order.service.mapper.OrderMapper.mapToOrderRowWithQuantity;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderRowRepository orderRowRepository;
    private final CartItemRepository cartItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    private final EmailClientService emailClientService;

    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto, Long userId) {
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDto.getPaymentId()).orElseThrow();
        Order newOrder = orderRepository.save(createNewOrder(orderDto, cart, shipment, payment, userId));
        saveOrderRows(cart, newOrder.getId(), shipment);
        clearOrderCart(orderDto);
        sendConfirmEmail(newOrder);
        return createOrderSummary(payment, newOrder);
    }

    private void sendConfirmEmail(Order newOrder) {
        emailClientService.getInstance()
                .send(newOrder.getEmail(),
                        "We have received your order",
                        createEmailMessage(newOrder));
    }

    private void clearOrderCart(OrderDto orderDto) {
        cartItemRepository.deleteByCartId(orderDto.getCartId());
        cartRepository.deleteCartById(orderDto.getCartId());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

    private void saveProductRows(Cart cart, Long orderId) {
        List<OrderRow> orderRows = cart.getItems().stream()
                .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem))
                .peek(orderRowRepository::save)
                .toList();
    }

    public List<OrderListDto> getOrdersForCustomer(Long userId) {
        return mapToOrderListDto(orderRepository.findByUserId(userId));
    }

}
