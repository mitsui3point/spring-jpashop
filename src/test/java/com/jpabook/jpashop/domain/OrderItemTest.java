package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemTest {

    private String name = "jpa book";
    private int stockQuantity = 10;
    private int price = 10000;
    private int count = 9;

    @Test
    void 주문상품_생성_상품재고차감() {
        //given
        Item book = getBookTestData(name, stockQuantity, price);
        int expected = stockQuantity - count;

        //when
        OrderItem orderItem = OrderItem.createOrderItem(book, price, count);
        int actual = orderItem.getItem().getStockQuantity();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문상품_취소_상품재고복원() {
        //given
        Item book = getBookTestData(name, stockQuantity, price);
        int expected = stockQuantity;
        OrderItem orderItem = OrderItem.createOrderItem(book, price, count);

        //when
        orderItem.cancel();
        int actual = orderItem.getItem().getStockQuantity();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private Item getBookTestData(String name, int stockQuantity, int price) {
        Item book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        return book;
    }
}