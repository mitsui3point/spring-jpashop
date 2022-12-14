package com.jpabook.jpashop.repository.simplequery;

import com.jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    @PersistenceContext
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new com.jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from orders o" +
                " join o.member m" +
                " join o.delivery d ", OrderSimpleQueryDto.class).getResultList();

    }
}
