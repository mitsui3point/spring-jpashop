package com.jpabook.jpashop.repository.order.query;

import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.order.query.dto.OrderItemQueryDto;
import com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Repository 관심사 분리
 *      : Entity(핵심 비즈니스 로직) // 화면 혹은 API 로직
 *      : 큰 두덩이로 분리
 *      : 핵심 비즈니스 로직과, 화면 혹은 API 로직의 LifeCycle 이 다르기 때문에 관심사 분리가 필요함.
 * {@link OrderRepository}
 *      : Order Entity 를 조회하기 위한 목적
 *      : 핵심 비즈니스 로직을 위한 Entity 를 조회할 때 주로 사용
 * {@link OrderSimpleQueryRepository}, {@link OrderQueryRepository}
 *      : 화면 혹은 API 에 의존관계가 있는 로직들을 떼어서 모아둔 Repository
 *      : 특정 화면이나 API 에 fit 한 query 들을 분리함, 특정화면 혹은 API 조회할 때 주로 사용
 *      : 보통 화면에 종속적인 로직들이 쿼리와 밀접한 관련이 있을 경우가 많기 때문에, 이를 분리하면 유지보수 관리에 용이하다.
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번
     * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findAllOneToNQuery() {
        List<OrderQueryDto> orders = findOrdersQuery();//query 1번 -> N 개
        orders.forEach(o -> {
            o.setOrderItemQueryDtos(
                    findOrderItemsQuery(o.getOrderId())//query N번
            );
        });
        return orders;
    }

    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private List<OrderQueryDto> findOrdersQuery() {
        return em.createQuery(
                        "select new com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from orders o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItemsQuery(Long orderId) {
        List<OrderItemQueryDto> result = em.createQuery(
                        "select new com.jpabook.jpashop.repository.order.query.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :id", OrderItemQueryDto.class)
                .setParameter("id", orderId)
                .getResultList();
        return result;
    }
}
