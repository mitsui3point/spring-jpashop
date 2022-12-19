package com.jpabook.jpashop.api.repository.simplequery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpabook.jpashop.OrderTestDataField;
import com.jpabook.jpashop.api.repository.simplequery.dto.OrderSimpleQueryDto;
import com.jpabook.jpashop.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class OrderSimpleQueryRepositoryTest extends OrderTestDataField {
    @Autowired
    private OrderSimpleQueryRepository orderSimpleQueryRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 주문전체_회원_배송_DTO_조회() throws JsonProcessingException {
        //given
        String expected = mapper.writeValueAsString(
                em.createQuery("select o from orders o", Order.class)
                        .getResultList()
                        .stream()
                        .map(order -> new OrderSimpleQueryDto(
                                order.getId(),
                                order.getMember().getName(),
                                order.getOrderDate(),
                                order.getStatus(),
                                order.getDelivery().getAddress()
                        ))
                        .collect(Collectors.toList())
        );

        //when
        String actual = mapper.writeValueAsString(orderSimpleQueryRepository.findOrderDtos());

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
