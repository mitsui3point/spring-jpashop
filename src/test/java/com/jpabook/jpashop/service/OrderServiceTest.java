package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.enums.DeliveryStatus;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.exception.NotEnoughItemStockException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void 주문() {
        //given
        int count = 5;
        int stockQuantity = 10;
        OrderStatus expectedStatus = OrderStatus.ORDER;
        int expectedStockQuantity = stockQuantity - count;

        Long memberId = persistMember("memberA",
                Address.builder()
                        .city("서울")
                        .street("스트릿")
                        .zipcode("123-123")
                        .build()
        );
        Long itemId = persistBookItem("jpa book1",
                stockQuantity,
                10000);
        Member expectedMember = em.find(Member.class, memberId);
        Item expectedItem = em.find(Book.class, itemId);

        //when
        Long orderId = orderService.order(memberId, itemId, count);

        Order actual = orderService.findOne(orderId);
        Member actualMember = actual.getMember();
        Item actualItem = actual.getOrderItems()
                .get(0)
                .getItem();
        int actualStockQuantity = actual.getOrderItems()
                .get(0)
                .getItem()
                .getStockQuantity();
        OrderStatus actualStatus = actual.getStatus();

        //then
        assertThat(actualMember).isEqualTo(expectedMember);
        assertThat(actualItem).isEqualTo(expectedItem);
        assertThat(actualStockQuantity).isEqualTo(expectedStockQuantity);
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    void 주문_실패() {
        //given
        int outOfRangeCount = 11;
        int stockQuantity = 10;
        Long memberId = persistMember("memberA",
                Address.builder()
                        .city("서울")
                        .street("스트릿")
                        .zipcode("123-123")
                        .build()
        );
        Long itemId = persistBookItem("jpa book1",
                stockQuantity,
                10000
        );

        //then
        assertThatExceptionOfType(NotEnoughItemStockException.class)
                .isThrownBy(() -> {
                    //when
                    Long orderId = orderService.order(memberId, itemId, outOfRangeCount);
                });
    }

    @Test
    void 주문_취소() {
        //given
        int count = 5;
        int expected = 10;
        OrderStatus expectedStatus = OrderStatus.CANCEL;
        Long memberId = persistMember("memberA",
                Address.builder()
                        .city("서울")
                        .street("스트릿")
                        .zipcode("123-123")
                        .build()
        );
        Long itemId = persistBookItem("jpa book1",
                expected,
                10000
        );
        Long orderId = orderService.order(memberId,
                itemId,
                count
        );

        //when
        orderService.cancel(orderId);
        Order order = orderService.findOne(orderId);
        int actual = order.getOrderItems()
                .get(0)
                .getItem()
                .getStockQuantity();
        OrderStatus actualStatus = order.getStatus();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    void 주문_취소_실패_예외() {
        //given
        int count = 5;
        int expected = 10;
        Long memberId = persistMember("memberA",
                Address.builder()
                        .city("서울")
                        .street("스트릿")
                        .zipcode("123-123")
                        .build()
        );
        Long itemId = persistBookItem("jpa book1",
                expected,
                10000
        );
        Long orderId = orderService.order(
                memberId,
                itemId,
                count
        );

        orderService.findOne(orderId)
                .getDelivery()
                .changeDeliveryStatus(DeliveryStatus.COMP);

        //then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    //when
                    orderService.cancel(orderId);
                }).withMessageContaining("배송이 완료된 상품은 주문취소가 불가능합니다.");
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }

    private Long persistMember(String name, Address address) {
        Member member = Member.builder()
                .name(name)
                .address(address)
                .build();

        em.persist(member);

        return member.getId();
    }

    private Long persistBookItem(String name, int stockQuantity, int price) {
        Item book = Book.builder()
                .name(name)
                .stockQuantity(stockQuantity)
                .price(price)
                .build();

        em.persist(book);

        return book.getId();
    }
}
