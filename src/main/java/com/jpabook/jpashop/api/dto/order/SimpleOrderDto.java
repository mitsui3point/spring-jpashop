package com.jpabook.jpashop.api.dto.order;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SimpleOrderDto {
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