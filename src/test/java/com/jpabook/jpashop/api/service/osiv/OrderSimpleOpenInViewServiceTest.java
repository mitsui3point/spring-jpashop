package com.jpabook.jpashop.api.service.osiv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.order.SimpleOrderDto;
import com.jpabook.jpashop.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderSimpleOpenInViewServiceTest extends OrderTestDataField {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OrderSimpleOpenInViewService orderSimpleOpenInViewService;
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
        List<Order> actual = orderSimpleOpenInViewService.ordersV1();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void ordersV2() throws JsonProcessingException {
        //given
        List<SimpleOrderDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(o -> SimpleOrderDto.builder()
                        .order(o)
                        .build()
                )
                .collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<SimpleOrderDto> actual = orderSimpleOpenInViewService.ordersV2();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void ordersV3() throws JsonProcessingException {
        //given
        List<SimpleOrderDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(o -> SimpleOrderDto.builder()
                        .order(o)
                        .build()
                )
                .collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        List<SimpleOrderDto> actual = orderSimpleOpenInViewService.ordersV3();
        String actualJson = mapper.writeValueAsString(actual);

        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}
