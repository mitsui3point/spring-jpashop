package com.jpabook.jpashop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.order.OrderApiDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import com.jpabook.jpashop.domain.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional(readOnly = true)
public class OrderApiControllerTest extends OrderTestDataField {

    private static final String BASE_URL = "/api";
    private static final String ORDER_GET_V1_URL = BASE_URL + "/v1/orders";
    private static final String ORDER_GET_V2_URL = BASE_URL + "/v2/orders";
    private static final String ORDER_GET_V3_URL = BASE_URL + "/v3/orders";
    private static final String ORDER_GET_V3_1_URL = BASE_URL + "/v3.1/orders";
    private static final String ORDER_GET_V4_URL = BASE_URL + "/v4/orders";
    private static final String ORDER_GET_V5_URL = BASE_URL + "/v5/orders";
    private static final String ORDER_GET_V6_URL = BASE_URL + "/v6/orders";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EntityManager em;

    private List<Order> expectedOrders;

    @BeforeEach
    void setUp() {
        init();
        apiInit();
    }

    private void apiInit() {
        expectedOrders = em.createQuery("select o from orders o ", Order.class)
                .getResultList();
    }

    @Test
    void 주문목록_상세_조회_V1() throws Exception {
        //given
        orderObjectGraph(expectedOrders);
        String expected = mapper.writeValueAsString(expectedOrders);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V1_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V2() throws Exception {
        //given
        List<OrderApiDto> expectedApiDtos = expectedOrders
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V2_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V3() throws Exception {
        //given
        List<OrderApiDto> expectedApiDtos = expectedOrders
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V3_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V3_1() throws Exception {
        //given
        int offset = 1;
        int limit = 100;
        List<OrderApiDto> expectedApiDtos = em.createQuery("select o from orders o ", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(
                get(ORDER_GET_V3_1_URL)
                        .queryParam("offset", String.valueOf(offset))
                        .queryParam("limit", String.valueOf(limit))
        );

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V4() throws Exception {
        //given
        List<OrderQueryDto> expectedApiDtos = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderQueryDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V4_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V5() throws Exception {
        //given
        List<OrderQueryDto> expectedApiDtos = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderQueryDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V5_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문목록_상세_조회_V6() throws Exception {
        //given
        List<OrderQueryDto> expectedApiDtos = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderQueryDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expected = mapper.writeValueAsString(expectedApiDtos);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V6_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
