package com.jpabook.jpashop.service.order.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto;
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
}