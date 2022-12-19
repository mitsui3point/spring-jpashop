package com.jpabook.jpashop.api.repository.query.dto;

import com.jpabook.jpashop.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemQueryDto {
//    @JsonIgnore
    private Long orderId;
    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    @Builder
    public OrderItemQueryDto(OrderItem orderItem) {
        this.orderId = orderItem.getOrder().getId();
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
