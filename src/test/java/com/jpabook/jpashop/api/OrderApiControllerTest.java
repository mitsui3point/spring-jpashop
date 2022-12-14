package com.jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.dto.order.OrderApiDto;
import com.jpabook.jpashop.domain.Order;
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
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EntityManager em;

    @Test
    void 주문목록_상세_조회_V1() throws Exception {
        //given
        List<Order> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList();
        expected.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().forEach(orderItem -> {
                orderItem.getItem().getName();
            });
        });
        String expectedJson = mapper.writeValueAsString(expected);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V1_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void 주문목록_상세_조회_V2() throws Exception {
        //given
        List<OrderApiDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expectedJson = mapper.writeValueAsString(expected);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V2_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void 주문목록_상세_조회_V3() throws Exception {
        //given
        List<OrderApiDto> expected = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        String expectedJson = mapper.writeValueAsString(expected);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_V3_URL));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
