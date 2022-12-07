package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문가격

    private int count;//주문수량

    @Builder
    public OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;

        item.subtractStockQuantity(count);
    }

    //==생성자 메서드==//
//    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
//        OrderItem orderItem = new OrderItem();
//        orderItem.setItem(item);
//        orderItem.setOrderPrice(orderPrice);
//        orderItem.setCount(count);
//
//        item.subtractStockQuantity(count);
//        return orderItem;
//    }

    //==비즈니스 메서드==//
    public void cancel() {
        getItem().addStockQuantity(this.count);
    }

    //==변경감지 메서드==//

    public void changeOrder(Order order) {
        this.order = order;
    }
}
