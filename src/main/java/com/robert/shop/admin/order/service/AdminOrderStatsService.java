package com.robert.shop.admin.order.service;

import com.robert.shop.admin.order.model.AdminOrder;
import com.robert.shop.admin.order.model.dto.AdminOrderStats;
import com.robert.shop.admin.order.repository.AdminOrderRepository;
import com.robert.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class AdminOrderStatsService {

    private final AdminOrderRepository adminOrderRepository;

    public AdminOrderStats getStatistics() {
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        List<AdminOrder> orders = adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(
                from,
                to,
                OrderStatus.COMPLETED
        );

        TreeMap<Integer, AdminOrderStatsValue> result = IntStream
                .rangeClosed(from.getDayOfMonth(), to.getDayOfMonth())
                .boxed()
                .map(i -> aggregateValues(i, orders))
                .collect(toMap(
                        key -> key.day(),
                        value -> value,
                        (t1, t2) -> {
                            throw new IllegalArgumentException();
                        },
                        TreeMap::new
                ));


        return AdminOrderStats.builder()
                .label(result.keySet().stream().toList())
                .sale(result.values().stream().map(s -> s.sales).toList())
                .order(result.values().stream().map(o -> o.orders).toList())
                .totalSales(result.values().stream().map(s -> s.sales).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .totalOrders(result.values().stream().map(o -> o.orders).reduce(Long::sum).orElse(0L))
                .build();
    }

    private AdminOrderStatsValue aggregateValues(Integer i, List<AdminOrder> orders) {
        return orders.stream()
                .filter(adminOrder -> adminOrder.getPlaceDate().getDayOfMonth() == i)
                .map(AdminOrder::getGrossValue)
                .reduce(new AdminOrderStatsValue(i, BigDecimal.ZERO, 0L),
                        (AdminOrderStatsValue o, BigDecimal v) -> new AdminOrderStatsValue(i, o.sales().add(v), o.orders() + 1),
                        (o1, o2) -> null
                );
    }

    private record AdminOrderStatsValue(Integer day, BigDecimal sales, Long orders) {
    }
}
