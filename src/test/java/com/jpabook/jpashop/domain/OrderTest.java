package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderTest {
    private String book1Name = "jpa book";
    private int book1StockQuantity = 10;
    private int book1Price = 10000;
    private int book1Count = 9;
    private String book2Name = "jpa book2";
    private int book2StockQuantity = 299;
    private int book2Price = 10000;
    private int book2Count = 290;

    private Address address = new Address("서울", "스트릿", "123-123");
    private Member member = new Member();
    private Delivery delivery = new Delivery();
    private OrderStatus orderStatus = OrderStatus.ORDER;
    private Item book1;
    private Item book2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private OrderItem[] orderItems;

    @BeforeEach
    void setUp() {
        member.setAddress(address);
        delivery.setAddress(address);

        book1 = getBookTestData(book1Name, book1StockQuantity, book1Price);
        book2 = getBookTestData(book2Name, book2StockQuantity, book2Price);
        orderItem1 = OrderItem.createOrderItem(book1, book1Price, book1Count);
        orderItem2 = OrderItem.createOrderItem(book2, book2Price, book2Count);
        orderItems = new OrderItem[]{orderItem1, orderItem2};
    }

    @Test
    void 주문_생성_상품재고차감() {
        //given
        int expectedCountSum = book1Count + book2Count;
        int expectedStockQuantitySum = book1StockQuantity + book2StockQuantity - expectedCountSum;

        //when
        Order order = Order.createOrder(member, delivery, orderItems);
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
        Order order = Order.createOrder(member, delivery, orderItems);
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
                .mapToInt(orderItem -> orderItem.getItem().getStockQuantity())
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
        delivery.setDeliveryStatus(DeliveryStatus.COMP);
        Order order = Order.createOrder(member, delivery, orderItems);
        //then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    //when
                    order.cancel();
                }).withMessageContaining("배송이 완료된 상품은 주문취소가 불가능합니다.");
    }

    private Item getBookTestData(String name, int stockQuantity, int price) {
        Item book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        return book;
    }
}
