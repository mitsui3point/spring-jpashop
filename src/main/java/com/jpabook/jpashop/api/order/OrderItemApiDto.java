package com.jpabook.jpashop.api.order;

import com.jpabook.jpashop.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemApiDto {
    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    @Builder
    private OrderItemApiDto(OrderItem orderItem) {
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
