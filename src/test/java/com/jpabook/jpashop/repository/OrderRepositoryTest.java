package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrderRepositoryTest extends OrderTestDataField {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 주문_데이터_저장() {
        //given
        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        em.persist(member1);
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
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        em.persist(member1);
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

        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        em.persist(member1);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.builder()
                .member(member2)
                .delivery(delivery2)
                .orderItems(orderItems2)
                .build();
        em.persist(member2);
        em.persist(book3);
        em.persist(book4);
        orderRepository.save(order2);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(order2);

        //when
        List<Order> actual = orderRepository.findAllByString(orderSearch);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_전체_조회_criteria() {
        //given
        Order order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        em.persist(member1);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        Order order2 = Order.builder()
                .member(member2)
                .delivery(delivery2)
                .orderItems(orderItems2)
                .build();
        em.persist(member2);
        em.persist(book3);
        em.persist(book4);
        orderRepository.save(order2);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(order2);

        //when
        List<Order> actual = orderRepository.findAllByCriteria(orderSearch);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional(readOnly = true)
    void 주문전체_회원_배송_조회() {
        //given
        List<Map> expected = em.createQuery("select o from orders o", Order.class)
                .getResultList()
                .stream()
                .map(this::getHashMapOrderMemberDelivery)
                .collect(Collectors.toList());

        //when
        List<Map> actual = orderRepository.findAllWithMemberDelivery()
                .stream()
                .map(this::getHashMapOrderMemberDelivery)
                .collect(Collectors.toList());

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional(readOnly = true)
    void 주문전체_상품목록() {
        //given
        List<Order> expected = em.createQuery("select o from orders o", Order.class)
                .getResultList();
        orderObjectGraph(expected);

        //when
        List<Order> actual = orderRepository.findAllWithItem();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional(readOnly = true)
    void 주문페이징_상품목록() {
        //given
        int offset = 1;
        int limit = 100;
        List<Order> expected = em.createQuery("select o from orders o", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        orderObjectGraph(expected);

        //when
        List<Order> actual = orderRepository.findPagingWithItem(offset, limit);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
