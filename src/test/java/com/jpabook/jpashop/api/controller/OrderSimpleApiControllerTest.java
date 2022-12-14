package com.jpabook.jpashop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.order.SimpleOrderDto;
import com.jpabook.jpashop.domain.Order;
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
class OrderSimpleApiControllerTest extends OrderTestDataField {
    private static final String BASE_URL = "/api";
    private static final String ORDER_GET_V1_URL = BASE_URL + "/v1/simple-orders";
    private static final String ORDER_GET_V2_URL = BASE_URL + "/v2/simple-orders";
    private static final String ORDER_GET_V3_URL = BASE_URL + "/v3/simple-orders";
    private static final String ORDER_GET_V4_URL = BASE_URL + "/v4/simple-orders";

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private EntityManager em;

    private List<Order> orders;

    private List<SimpleOrderDto> simpleOrderDtos;

    @BeforeEach
    void setUp() {
        init();

        simpleApiInit();
    }

    @Test
    void 주문_전체조회_V1() throws Exception {
        //given
        String expected = mapper.writeValueAsString(orders);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V1_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문_전체조회_V2() throws Exception {
        //given
        String expected = mapper.writeValueAsString(simpleOrderDtos);
        em.flush();
        em.clear();

        //when
        System.out.println("when==================================");
        ResultActions perform = mvc.perform(get(ORDER_GET_V2_URL));
        System.out.println("//when==================================");

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문_전체조회_V3() throws Exception {
        //given
        String expected = mapper.writeValueAsString(simpleOrderDtos);

        //when
        System.out.println("when==================================");
        ResultActions perform = mvc.perform(get(ORDER_GET_V3_URL));
        System.out.println("//when==================================");

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문_전체조회_V4() throws Exception {
        //given
        String expected = mapper.writeValueAsString(simpleOrderDtos);

        //when
        System.out.println("when==================================");
        ResultActions perform = mvc.perform(get(ORDER_GET_V4_URL));
        System.out.println("//when==================================");

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    private void simpleApiInit() {
        String query = "select o from orders o ";

        System.out.println("orders==================================");
        orders = em.createQuery(query, Order.class)
                .getResultList();
        orderObjectGraph(orders);
        System.out.println("//orders==================================");

        System.out.println("simpleOrderDtos==================================");
        simpleOrderDtos = em.createQuery(query, Order.class)
                .getResultList()
                .stream()
                .map(o -> SimpleOrderDto.builder()
                        .order(o)
                        .build()
                )
                .collect(Collectors.toList());
        System.out.println("//simpleOrderDtos==================================");
    }
}