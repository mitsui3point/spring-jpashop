package com.jpabook.jpashop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper om;

    private Order order;
    private Order order2;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 주문_데이터_저장() {
        //given
        order = Order.builder()
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
        saveTestOrders();
//        order = Order.builder()
//                .member(member1)
//                .delivery(delivery1)
//                .orderItems(orderItems1)
//                .build();
//        em.persist(member1);
//        em.persist(book1);
//        em.persist(book2);
//        orderRepository.save(order);
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
    void 주문_전체_조회_JPQL() {
        //given
        saveTestOrders();

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
        saveTestOrders();

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(order2);

        //when
        List<Order> actual = orderRepository.findAllByCriteria(orderSearch);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_전체_조회_querydsl() {
        //given
        saveTestOrders();

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName(member2.getName());
        List<Order> expected = Arrays.asList(order2);

        //when
        List<Order> actual = orderRepository.findAll(orderSearch);

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
    void 주문페이징_상품목록() throws JsonProcessingException {
        //given
        int offset = 0;
        int limit = 100;
        log.info("expected/=========================================");
        List<Order> expected = em.createQuery("select o from orders o", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        orderObjectGraph(expected);
        String expectedValue = om.writeValueAsString(expected);
        log.info("=========================================expected/");

        //when
        log.info("actual/=========================================");
        List<Order> actual = orderRepository.findPagingWithItem(offset, limit);
        String actualValue = om.writeValueAsString(actual);
        log.info("=========================================actual/");
        log.info("expectedValue:{}", expectedValue);
        log.info("actualValue:{}", actualValue);

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    private void saveTestOrders() {
        order = Order.builder()
                .member(member1)
                .delivery(delivery1)
                .orderItems(orderItems1)
                .build();
        em.persist(member1);
        em.persist(book1);
        em.persist(book2);
        orderRepository.save(order);

        order2 = Order.builder()
                .member(member2)
                .delivery(delivery2)
                .orderItems(orderItems2)
                .build();
        em.persist(member2);
        em.persist(book3);
        em.persist(book4);
        orderRepository.save(order2);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
