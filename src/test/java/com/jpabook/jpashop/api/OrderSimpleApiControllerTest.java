package com.jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional(readOnly = true)
class OrderSimpleApiControllerTest {
    private static final String BASE_URL = "/api";
    private static final String ORDER_GET_URL = BASE_URL + "/v1/simple-orders";
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private EntityManager em;

    private Order[] initOrders;

    @BeforeEach
    void setUp() {
        initOrders = em.createQuery("select o from orders o join o.member m ",
                        Order.class)
                .getResultList()
                .toArray(Order[]::new);
        initOrderObjectGraph();
    }

    private void initOrderObjectGraph() {
        for (Order initOrder : initOrders) {
            initOrder.getMember().getName();
            initOrder.getDelivery().getDeliveryStatus();
            for (OrderItem orderItem : initOrder.getOrderItems()) {
                orderItem.getOrderPrice();
                orderItem.getItem().getName();
                for (Category category : orderItem.getItem().getCategories()) {
                    category.getName();
                }
            }
        }
    }

    @Test
    void 주문_전체조회_V1() throws Exception {
        //given
        String expected = mapper.writeValueAsString(initOrders);

        //when
        ResultActions perform = mvc.perform(get(ORDER_GET_URL));

        //then
        perform.andDo(print())
                .andExpect(content().json(expected));
    }

}