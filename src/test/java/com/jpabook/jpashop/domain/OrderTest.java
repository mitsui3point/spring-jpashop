package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.enums.DeliveryStatus;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderTest {
    private final String book1Name = "jpa book";
    private final int book1StockQuantity = 10;
    private final int book1Price = 10000;
    private final int book1Count = 9;
    private final String book2Name = "jpa book2";
    private final int book2StockQuantity = 299;
    private final int book2Price = 10000;
    private final int book2Count = 290;

    private final Address address = Address.builder()
            .city("서울")
            .street("스트릿")
            .zipcode("123-123")
            .build();
    private final Member member = Member.builder()
            .address(address)
            .build();
    private final Delivery delivery = Delivery.builder()
            .address(address)
            .build();
    private final OrderStatus orderStatus = OrderStatus.ORDER;
    private final Item book1 = Book.builder()
            .name(book1Name)
            .stockQuantity(book1StockQuantity)
            .price(book1Price)
            .build();
    private final Item book2 = Book.builder()
            .name(book2Name)
            .stockQuantity(book2StockQuantity)
            .price(book2Price)
            .build();
    private final OrderItem orderItem1 = OrderItem.builder()
            .item(book1)
            .orderPrice(book1Price)
            .count(book1Count)
            .build();
    private final OrderItem orderItem2 = OrderItem.builder()
            .item(book2)
            .orderPrice(book2Price)
            .count(book2Count)
            .build();
    private final OrderItem[] orderItems = new OrderItem[]{orderItem1, orderItem2};

    @Test
    void 주문_생성_상품재고차감() {
        //given
        int expectedCountSum = book1Count + book2Count;
        int expectedStockQuantitySum = book1StockQuantity + book2StockQuantity - expectedCountSum;

        //when
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .status(orderStatus)
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
        assertThat(actualMember).isEqualTo(member);
        assertThat(actualDelivery).isEqualTo(delivery);
        assertThat(actualOrderItems).containsOnly(orderItems);
        assertThat(actualOrderStatus).isEqualTo(orderStatus);

        assertThat(actualCountSum).isEqualTo(expectedCountSum);
        assertThat(actualStockQuantitySum).isEqualTo(expectedStockQuantitySum);
    }

    @Test
    void 주문_취소_상품재고복원() {
        //given
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
        int countSum = Arrays.stream(orderItems)
                .mapToInt(orderItem -> orderItem.getCount())
                .sum();
        int stockQuantitySum = Arrays.stream(orderItems)
                .mapToInt(orderItem -> orderItem.getItem().getStockQuantity())
                .sum();
        int expected = countSum + stockQuantitySum;
        OrderStatus expectedOrderStatus = OrderStatus.CANCEL;
        System.out.println("countSum = " + countSum);
        System.out.println("stockQuantitySum = " + stockQuantitySum);
        System.out.println("expected = " + expected);


        //when
        order.cancel();
        int actual = Arrays.stream(orderItems)
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
        delivery.changeDeliveryStatus(DeliveryStatus.COMP);
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
        //then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    //when
                    order.cancel();
                }).withMessageContaining("배송이 완료된 상품은 주문취소가 불가능합니다.");
    }
}
