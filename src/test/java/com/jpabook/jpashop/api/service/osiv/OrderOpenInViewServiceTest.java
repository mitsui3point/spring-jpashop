package com.jpabook.jpashop.api.service.osiv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.order.OrderApiDto;
import com.jpabook.jpashop.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderOpenInViewServiceTest extends OrderTestDataField {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OrderOpenInViewService orderOpenInViewService;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void ordersV1() throws JsonProcessingException {
        //given
        List<Order> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList();
        orderObjectGraph(expected);
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<Order> actual = orderOpenInViewService.ordersV1();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void ordersV2() throws JsonProcessingException {
        //given
        List<OrderApiDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<OrderApiDto> actual = orderOpenInViewService.ordersV2();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void ordersV3() throws JsonProcessingException {
        //given
        List<OrderApiDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<OrderApiDto> actual = orderOpenInViewService.ordersV3();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void ordersV3_1() throws JsonProcessingException {
        //given
        int offset = 1;
        int limit = 100;
        List<OrderApiDto> expected = em.createQuery("select o from orders o ", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<OrderApiDto> actual = orderOpenInViewService.ordersV3_1(offset, limit);
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}
