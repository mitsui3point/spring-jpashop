package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager em;
    private final String book1Name = "jpa book";
    private final int book1StockQuantity = 10;
    private final int book1Price = 10000;
    private final int book1Count = 9;
    private final String book2Name = "jpa book2";
    private final int book2StockQuantity = 299;
    private final int book2Price = 10000;
    private final int book2Count = 290;

    private final Address address = new Address("서울", "스트릿", "123-123");
    private final Member member = new Member();
    private final Delivery delivery = new Delivery();
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
    void 주문_데이터_저장() {
        //given
        Order order = Order.createOrder(member, delivery, orderItems);
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
        Order order = Order.createOrder(member, delivery, orderItems);
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);
        OrderStatus expected = OrderStatus.CANCEL;

        //when
        order.cancel();
        OrderStatus actual = orderRepository.findById(order.getId()).getStatus();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_전체_조회() {
        //given
        Member member2 = new Member();
        member2.setName("mem2");
        Delivery delivery2 = new Delivery();
        Item book3 = getBookTestData("jpa book3", 50, 10000);
        Item book4 = getBookTestData("jpa book4", 60, 10500);
        OrderItem[] orderItems = {
                OrderItem.createOrderItem(book3, 12000, 40),
                OrderItem.createOrderItem(book4, 13000, 50)
        };

        Order order = Order.createOrder(member, delivery, orderItems);
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.createOrder(member2, delivery2, orderItems);
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
        Member member2 = new Member();
        member2.setName("mem2");
        Delivery delivery2 = new Delivery();
        Item book3 = getBookTestData("jpa book3", 50, 10000);
        Item book4 = getBookTestData("jpa book4", 60, 10500);
        OrderItem[] orderItems = {
                OrderItem.createOrderItem(book3, 12000, 40),
                OrderItem.createOrderItem(book4, 13000, 50)
        };

        Order order = Order.createOrder(member, delivery, orderItems);
        em.persist(member);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.createOrder(member2, delivery2, orderItems);
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

    private Item getBookTestData(String name, int stockQuantity, int price) {
        Item book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        return book;
    }
}
