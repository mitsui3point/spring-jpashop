package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrderRepositoryTest {
    private final String book1Name = "jpa book";
    private final int book1StockQuantity = 10;
    private final int book1Price = 10000;
    private final int book1Count = 9;
    private final String book2Name = "jpa book2";
    private final int book2StockQuantity = 299;
    private final int book2Price = 10000;
    private final int book2Count = 290;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager em;
    private Address address = Address.builder()
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
    private Item book1 = Book.builder()
            .name(book1Name)
            .stockQuantity(book1StockQuantity)
            .price(book1Price)
            .build();
    private OrderItem orderItem1 = OrderItem.builder()
            .item(book1)
            .orderPrice(book1Price)
            .count(book1Count)
            .build();
    private Item book2 = Book.builder()
            .name(book2Name)
            .stockQuantity(book2StockQuantity)
            .price(book2Price)
            .build();
    private OrderItem orderItem2 = OrderItem.builder()
            .item(book2)
            .orderPrice(book2Price)
            .count(book2Count)
            .build();
    private OrderItem[] orderItems = new OrderItem[]{orderItem1, orderItem2};

    @Test
    void 주문_데이터_저장() {
        //given
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
        em.persist(member);
        em.persist(book1);
        em.persist(book2);

        //when
        orderRepository.save(order);
        Order actual = orderRepository.findById(order.getId());

        //then
        assertThat(actual).isEqualTo(order);
    }

    @Test
    void 주문_데이터_취소() {
        //given
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();//member, delivery, orderItems
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);
        OrderStatus expected = OrderStatus.CANCEL;

        //when
        order.cancel();
        OrderStatus actual = orderRepository.findById(
                order.getId()
        )
                .getStatus();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_전체_조회() {
        //given
        Member member2 = Member.builder()
                .name("mem2")
                .address(address)
                .build();
        member2.changeName("mem2");
        Address address2 = Address.builder()
                .city("서울")
                .street("스트릿")
                .zipcode("123-123")
                .build();
        Delivery delivery2 = Delivery.builder()
                .address(address2)
                .build();
        Item book3 = Book.builder()
                .name("jpa book3")
                .stockQuantity(50)
                .price(10000)
                .build();//getBookTestData("jpa book3", 50, 10000);
        Item book4 = Book.builder()
                .name("jpa book4")
                .stockQuantity(60)
                .price(10500)
                .build();//getBookTestData("jpa book4", 60, 10500);
        OrderItem[] orderItems = {
                OrderItem.builder()
                        .item(book3)
                        .orderPrice(12000)
                        .count(40)
                        .build(),//(book3, 12000, 40),
                OrderItem.builder()
                        .item(book4)
                        .orderPrice(13000)
                        .count(50)
                        .build()//(book4, 13000, 50)
        };

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.builder()
                .member(member2)
                .delivery(delivery2)
                .orderItems(orderItems)
                .build();
        em.persist(member2);
        em.persist(book3);
        em.persist(book4);
        orderRepository.save(order2);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(new Order[]{order2});

        //when
        List<Order> actual = orderRepository.findAllByString(orderSearch);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_전체_조회_criteria() {
        //given
        Member member2 = Member.builder()
                .name("mem2")
                .address(address)
                .build();
        member2.changeName("mem2");
        Address address2 = Address.builder()
                .city("서울")
                .street("스트릿")
                .zipcode("123-123")
                .build();
        Delivery delivery2 = Delivery.builder()
                .address(address2)
                .build();
        Item book3 = Book.builder()
                .name("jpa book3")
                .stockQuantity(50)
                .price(10000)
                .build();//getBookTestData("jpa book3", 50, 10000);
        Item book4 = Book.builder()
                .name("jpa book4")
                .stockQuantity(60)
                .price(10500)
                .build();//getBookTestData("jpa book4", 60, 10500);
        OrderItem[] orderItems = {
                OrderItem.builder()
                        .item(book3)
                        .orderPrice(12000)
                        .count(40)
                        .build(),//(book3, 12000, 40),
                OrderItem.builder()
                        .item(book4)
                        .orderPrice(13000)
                        .count(50)
                        .build()//(book4, 13000, 50)
        };

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.builder()
                .member(member2)
                .delivery(delivery2)
                .orderItems(orderItems)
                .build();
        em.persist(member2);
        em.persist(book3);
        em.persist(book4);
        orderRepository.save(order2);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(new Order[]{order2});

        //when
        List<Order> actual = orderRepository.findAllByCriteria(orderSearch);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
