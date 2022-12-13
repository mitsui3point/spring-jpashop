package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.service.OrderService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();//lazy 강제 초기화
            for (OrderItem orderItem : order.getOrderItems()) {
                orderItem.getOrderPrice();//lazy 강제 초기화;
                orderItem.getItem().getName();//lazy 강제 초기화
                for (Category category : orderItem.getItem().getCategories()) {
                    category.getName();//lazy 강제 초기화
                }
            }
            order.getDelivery().getDeliveryStatus();//lazy 강제 초기화
        }
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N + 배송 N
        return orderService.findAll(new OrderSearch())
                .stream()
                .map(order ->
                        SimpleOrderDto.builder()
                            .order(order)
                            .build()
                )
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        return orderService.findAllWithMemberDelivery()
                .stream()
                .map(order ->
                        SimpleOrderDto.builder()
                                .order(order)
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        @Builder
        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();//lazy 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();//lazy 초기화
        }
    }
}