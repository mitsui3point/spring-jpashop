package com.jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

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
    void 주문전체_DTO_컬렉션_N_조회() throws JsonProcessingException {
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
        List<OrderQueryDto> actuals = orderQueryRepository.findAllOneToNQuery();
        String actual = om.writeValueAsString(actuals);

        //then
        assertThat(actual).isEqualTo(expected);
    }

}
