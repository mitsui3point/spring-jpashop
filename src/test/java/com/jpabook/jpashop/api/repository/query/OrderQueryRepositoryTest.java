package com.jpabook.jpashop.api.repository.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.api.repository.query.dto.OrderFlatDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderQueryRepositoryTest extends OrderTestDataField {
    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        init();
    }

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
        List<OrderQueryDto> actualities = orderQueryRepository.findAllEachQuery();
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
        List<OrderQueryDto> actualities = orderQueryRepository.findAllInQuery();
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
        List<OrderFlatDto> actualities = orderQueryRepository.findAllFlat();
        String actual = om.writeValueAsString(actualities);

        //then
        assertThat(actual).isEqualTo(expected);
    }

}
