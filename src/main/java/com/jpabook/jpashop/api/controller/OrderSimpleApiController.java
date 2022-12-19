package com.jpabook.jpashop.api.controller;

import com.jpabook.jpashop.api.order.SimpleOrderDto;
import com.jpabook.jpashop.api.service.osiv.OrderSimpleOpenInViewService;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.api.repository.simplequery.dto.OrderSimpleQueryDto;
import com.jpabook.jpashop.api.service.simplequery.OrderSimpleQueryService;
import com.jpabook.jpashop.service.OrderService;
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
    private final OrderService orderService;
    private final OrderSimpleOpenInViewService orderSimpleOpenInViewService;
    private final OrderSimpleQueryService orderSimpleQueryService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
//        List<Order> orders = orderService.findAll(new OrderSearch());
//        orders.forEach(order -> {
//            order.getMember().getName();//lazy 강제 초기화
//            order.getDelivery().getDeliveryStatus();//lazy 강제 초기화
//            order.getOrderItems().forEach(orderItem -> {
//                orderItem.getItem().getName();//lazy 강제 초기화
//            });
//        });
        return orderSimpleOpenInViewService.ordersV1();
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N + 배송 N
//        return orderService.findAll(new OrderSearch())
//                .stream()
//                .map(order ->
//                        SimpleOrderDto.builder()
//                                .order(order)
//                                .build()
//                )
//                .collect(Collectors.toList());
        return orderSimpleOpenInViewService.ordersV2();
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
//        return orderService.findAllWithMemberDelivery()
//                .stream()
//                .map(order ->
//                        SimpleOrderDto.builder()
//                                .order(order)
//                                .build()
//                )
//                .collect(Collectors.toList());
        return orderSimpleOpenInViewService.ordersV3();
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryService.findOrderDtos();
    }
}