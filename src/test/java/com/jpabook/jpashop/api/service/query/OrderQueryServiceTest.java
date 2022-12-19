package com.jpabook.jpashop.api.service.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.repository.query.dto.OrderFlatDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderItemQueryDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderQueryServiceTest extends OrderTestDataField {
    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper om;

    @Test
    void 주문전체_DTO_컬렉션_EACH_조회() throws JsonProcessingException {
        //given
        List<OrderQueryDto> expects = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderQueryDto.builder()
                        .order(order)
                        .build()
                )
                .collect(Collectors.toList());
        String expected = om.writeValueAsString(expects);

        //when
        List<OrderQueryDto> actualities = orderQueryService.findAllEachQuery();
        String actual = om.writeValueAsString(actualities);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문전체_DTO_컬렉션_IN_조회() throws JsonProcessingException {
        //given
        List<OrderQueryDto> expects = em.createQuery("select o from orders o ", Order.class)
                .getResultList()
                .stream()
                .map(order -> OrderQueryDto.builder()
                        .order(order)
                        .build()
                )
                .collect(Collectors.toList());
        String expected = om.writeValueAsString(expects);

        //when
        List<OrderQueryDto> actualities = orderQueryService.findAllInQuery();
        String actual = om.writeValueAsString(actualities);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문전체_DTO_컬렉션_FLAT_조회() throws JsonProcessingException {
        //given
        List<OrderFlatDto> expects = em.createQuery(
                        "select o, oi from orders o join o.orderItems oi ", Object[].class)
                .getResultList()
                .stream()
                .map(o -> OrderFlatDto
                        .builder()
                        .order(
                                (Order) Arrays.stream(o)
                                        .filter(obj -> obj instanceof Order)
                                        .findAny()
                                        .get()
                        )
                        .orderItem(
                                (OrderItem) Arrays.stream(o)
                                        .filter(obj -> obj instanceof OrderItem)
                                        .findAny()
                                        .get()
                        )
                        .build()
                )
                .collect(Collectors.toList());
        String expected = om.writeValueAsString(expects);

        //when
        List<OrderFlatDto> actualities = orderQueryService.findAllFlat();
        String actual = om.writeValueAsString(actualities);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문전체_DTO_컬렉션_FLAT_ORDER_QUERY_DTO_TRANSFER() throws JsonProcessingException {
        //given
        List<OrderQueryDto> expects = em.createQuery(
                        "select o, oi from orders o join o.orderItems oi ", Object[].class)
                .getResultList()
                .stream()
                .map(o -> OrderFlatDto
                        .builder()
                        .order(
                                (Order) Arrays.stream(o)
                                        .filter(obj -> obj instanceof Order)
                                        .findAny()
                                        .get()
                        )
                        .orderItem(
                                (OrderItem) Arrays.stream(o)
                                        .filter(obj -> obj instanceof OrderItem)
                                        .findAny()
                                        .get()
                        )
                        .build()
                )
                .collect(Collectors.toList())
                .stream()
                .collect(
                        groupingBy(
                                o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                                mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                        )
                )
                .entrySet()
                .stream()
                .map(e -> {
                    OrderQueryDto key = e.getKey();
                    return new OrderQueryDto(key.getOrderId(),
                            key.getName(), key.getOrderDate(), key.getOrderStatus(),
                            key.getAddress(), e.getValue());
                })
                .collect(toList());
        String expected = om.writeValueAsString(expects);

        //when
        List<OrderQueryDto> actualities = orderQueryService.findAllFlatApiV6();
        String actual = om.writeValueAsString(actualities);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
