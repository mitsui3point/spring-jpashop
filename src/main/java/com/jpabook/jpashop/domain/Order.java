package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.enums.DeliveryStatus;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;//주문상태[ORDER, CANCEL]

    @Builder
    private Order(Member member, Delivery delivery, OrderStatus status, OrderItem... orderItems) {
        this.member = member;
        this.delivery = delivery;
        this.status = status;
        for (OrderItem orderItem : orderItems) {
            this.addOrderItem(orderItem);
        }
        this.orderDate = LocalDateTime.now();
    }

    //==연관관계 메서드==//
    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    private void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    private void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.changeOrder(this);
    }

    //==비즈니스 메서드==//
    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("배송이 완료된 상품은 주문취소가 불가능합니다.");
        }
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
        this.status = OrderStatus.CANCEL;
    }
}
