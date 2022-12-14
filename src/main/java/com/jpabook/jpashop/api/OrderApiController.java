package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;

    /**
     * V1. 엔티티 직접 노출
     * - 엔티티가 변하면 API 스펙이 변한다.
     * - 트랜잭션 안에서 지연 로딩 필요
     * - 양방향 연관관계 문제
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findAll(new OrderSearch());
        orders.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems()
                    .forEach(orderItem -> {
                        orderItem.getItem().getName();
                    });
        });
        return orders;
    }
}
