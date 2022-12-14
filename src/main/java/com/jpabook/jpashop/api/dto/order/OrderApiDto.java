package com.jpabook.jpashop.api.dto.order;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderApiDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemApiDto> orderItems = new ArrayList<>();

    @Builder
    private OrderApiDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress();
        order.getOrderItems().forEach(orderItem -> {
            this.orderItems.add(
                    OrderItemApiDto.builder()
                            .orderItem(orderItem)
                            .build()
            );
        });
    }
}
