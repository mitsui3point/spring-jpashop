package com.jpabook.jpashop.api.controller;

import com.jpabook.jpashop.api.order.SimpleOrderDto;
import com.jpabook.jpashop.api.repository.simplequery.dto.OrderSimpleQueryDto;
import com.jpabook.jpashop.api.service.osiv.OrderSimpleOpenInViewService;
import com.jpabook.jpashop.api.service.simplequery.OrderSimpleQueryService;
import com.jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderSimpleOpenInViewService orderSimpleOpenInViewService;
    private final OrderSimpleQueryService orderSimpleQueryService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderSimpleOpenInViewService.ordersV1();
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N + 배송 N
        return orderSimpleOpenInViewService.ordersV2();
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        return orderSimpleOpenInViewService.ordersV3();
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryService.findOrderDtos();
    }
}