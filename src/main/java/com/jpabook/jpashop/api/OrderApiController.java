package com.jpabook.jpashop.api;

import com.jpabook.jpashop.api.dto.order.OrderApiDto;
import com.jpabook.jpashop.api.dto.order.OrderItemApiDto;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제
 *
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
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

    /**
     * V2. 엔티티를 DTO로 변환
     * 지연 로딩으로 너무 많은 SQL 실행
     */
    @GetMapping("/api/v2/orders")
    public List<OrderApiDto> ordersV2() {
        return orderService.findAll(new OrderSearch())
                .stream()
                .map(order -> OrderApiDto.builder()
                            .order(order)
                            .build()
                )
                .collect(Collectors.toList());
    }
}
