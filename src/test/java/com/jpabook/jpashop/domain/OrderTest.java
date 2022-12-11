package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.domain.enums.DeliveryStatus;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderTest extends OrderTestDataField {
    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 주문_생성_상품재고차감() {
        //given
        int expectedCountSum = book1OrderCount + book2OrderCount;
        int expectedStockQuantitySum = book1StockQuantity + book2StockQuantity - expectedCountSum;

        //when
        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .status(orderStatus1)
                .build();
        Member actualMember = order.getMember();
        Delivery actualDelivery = order.getDelivery();
        List<OrderItem> actualOrderItems = order.getOrderItems();
        OrderStatus actualOrderStatus = order.getStatus();

        int actualCountSum = order.getOrderItems()
                .stream()
                .mapToInt(orderItem -> orderItem.getCount())
                .sum();
        int actualStockQuantitySum = order.getOrderItems()
                .stream()
                .mapToInt(orderItem -> orderItem.getItem().getStockQuantity())
                .sum();

        //then
        assertThat(actualMember).isEqualTo(member1);
        assertThat(actualDelivery).isEqualTo(delivery1);
        assertThat(actualOrderItems).containsOnly(orderItems1);
        assertThat(actualOrderStatus).isEqualTo(orderStatus1);

        assertThat(actualCountSum).isEqualTo(expectedCountSum);
        assertThat(actualStockQuantitySum).isEqualTo(expectedStockQuantitySum);
    }

    @Test
    void 주문_취소_상품재고복원() {
        //given
        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        int countSum = Arrays.stream(orderItems1)
                .mapToInt(orderItem -> orderItem.getCount())
                .sum();
        int stockQuantitySum = Arrays.stream(orderItems1)
                .mapToInt(orderItem -> orderItem.getItem().getStockQuantity())
                .sum();
        int expected = countSum + stockQuantitySum;
        OrderStatus expectedOrderStatus = OrderStatus.CANCEL;
        System.out.println("countSum = " + countSum);
        System.out.println("stockQuantitySum = " + stockQuantitySum);
        System.out.println("expected = " + expected);


        //when
        order.cancel();
        int actual = Arrays.stream(orderItems1)
                .mapToInt(
                        orderItem -> orderItem
                                .getItem()
                                .getStockQuantity()
                )
                .sum();
        OrderStatus actualOrderStatus = order.getStatus();
        System.out.println("actual = " + actual);
        System.out.println("actualOrderStatus = " + actualOrderStatus);

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(actualOrderStatus).isEqualTo(expectedOrderStatus);
    }

    @Test
    void 주문_취소_배송완료_예외() {
        //given
        delivery1.changeDeliveryStatus(DeliveryStatus.COMP);
        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        //then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    //when
                    order.cancel();
                }).withMessageContaining("배송이 완료된 상품은 주문취소가 불가능합니다.");
    }
}
