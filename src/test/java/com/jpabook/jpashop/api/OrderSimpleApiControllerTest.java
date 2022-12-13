package com.jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
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

import static com.jpabook.jpashop.api.OrderSimpleApiController.SimpleOrderDto;
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

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private EntityManager em;

    private Order[] initOrders;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 주문_전체조회_V1() throws Exception {
        //given
        initOrders = em.createQuery("select o from orders o ",
                        Order.class)
                .getResultList()
                .toArray(Order[]::new);
        initOrderObjectGraph(initOrders);
        String expected = mapper.writeValueAsString(initOrders);

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
        System.out.println("==================================given==================================");
        SimpleOrderDto[] initOrderDto = em.createQuery("select o from orders o join o.member m ", Order.class)
                .getResultList()
                .stream()
                .map(o -> SimpleOrderDto.builder()
                        .order(o)
                        .build()
                )
                .toArray(SimpleOrderDto[]::new);
        System.out.println("==================================given==================================");

        String expected = mapper.writeValueAsString(initOrderDto);
        em.flush();
        em.clear();

        //when
        System.out.println("==================================when==================================");
        ResultActions perform = mvc.perform(get(ORDER_GET_V2_URL));
        System.out.println("==================================when==================================");

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 주문_전체조회_V3() throws Exception {
        //given
        SimpleOrderDto[] initOrderDto = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(o -> SimpleOrderDto.builder()
                        .order(o)
                        .build()
                )
                .toArray(SimpleOrderDto[]::new);

        String expected = mapper.writeValueAsString(initOrderDto);

        //when
        System.out.println("==================================when==================================");
        ResultActions perform = mvc.perform(get(ORDER_GET_V3_URL));
        System.out.println("==================================when==================================");

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}