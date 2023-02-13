package com.robert.shop.order.service;

import com.robert.shop.common.mail.EmailClientService;
import com.robert.shop.common.mail.FakeEmailService;
import com.robert.shop.common.model.Cart;
import com.robert.shop.common.model.CartItem;
import com.robert.shop.common.model.Product;
import com.robert.shop.common.repository.CartItemRepository;
import com.robert.shop.common.repository.CartRepository;
import com.robert.shop.order.dto.OrderDto;
import com.robert.shop.order.dto.OrderSummary;
import com.robert.shop.common.model.OrderStatus;
import com.robert.shop.order.model.Payment;
import com.robert.shop.order.model.PaymentType;
import com.robert.shop.order.model.Shipment;
import com.robert.shop.order.repository.OrderRepository;
import com.robert.shop.order.repository.OrderRowRepository;
import com.robert.shop.order.repository.PaymentRepository;
import com.robert.shop.order.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderRowRepository orderRowRepository;
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    EmailClientService emailClientService;

    @Test
    void shouldPlaceOrder() {
        //given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(createCart());
        when(shipmentRepository.findById(any())).thenReturn(createShipment());
        when(paymentRepository.findById(any())).thenReturn(createPayment());
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(emailClientService.getInstance()).thenReturn(new FakeEmailService());
        Long userId = 1L;
        //when
        OrderSummary orderSummary = orderService.placeOrder(orderDto, userId);
        //then
        assertThat(orderSummary).isNotNull();
        assertThat(orderSummary.getStatus()).isEqualTo(OrderStatus.NEW);
        assertThat(orderSummary.getGrossValue()).isEqualTo(new BigDecimal("34.22"));
        assertThat(orderSummary.getPayment().getType()).isEqualTo(PaymentType.BANK_TRANSFER);
        assertThat(orderSummary.getPayment().getName()).isEqualTo("test payment");
        assertThat(orderSummary.getPayment().getId()).isEqualTo(1L);
    }

    private OrderDto createOrderDto() {
        return OrderDto.builder()
                .firstname("Bob")
                .lastname("Dylan")
                .street("Ocean")
                .zipcode("33-333")
                .city("Las Vegas")
                .email("bdylan@gmail.com")
                .phone("333222111")
                .cartId(1L)
                .shipmentId(2L)
                .paymentId(3L)
                .build();
    }

    private Optional<Cart> createCart() {
        return Optional.of(Cart.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(createItems())
                .build());
    }

    private List<CartItem> createItems() {
        return List.of(
                CartItem.builder()
                        .id(1L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(1L)
                                .price(new BigDecimal("10.11"))
                                .build())
                        .build(),
                CartItem.builder()
                        .id(2L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(2L)
                                .price(new BigDecimal("10.11"))
                                .build())
                        .build()
        );
    }

    private Optional<Shipment> createShipment() {
        return Optional.of(Shipment.builder()
                .id(2L)
                .price(new BigDecimal("14.00"))
                .build());
    }

    private Optional<Payment> createPayment() {
        return Optional.of(Payment.builder()
                .id(1L)
                .name("test payment")
                .type(PaymentType.BANK_TRANSFER)
                .defaultPayment(true)
                .build());
    }

}