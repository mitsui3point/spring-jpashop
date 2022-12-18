package com.jpabook.jpashop.api.repository.query;

import com.jpabook.jpashop.api.repository.query.dto.OrderFlatDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderItemQueryDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import com.jpabook.jpashop.api.repository.simplequery.OrderSimpleQueryRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository 관심사 분리
 * : Entity(핵심 비즈니스 로직) // 화면 혹은 API 로직
 * : 큰 두덩이로 분리
 * : 핵심 비즈니스 로직과, 화면 혹은 API 로직의 LifeCycle 이 다르기 때문에 관심사 분리가 필요함.
 * {@link OrderRepository}
 * : Order Entity 를 조회하기 위한 목적
 * : 핵심 비즈니스 로직을 위한 Entity 를 조회할 때 주로 사용
 * {@link OrderSimpleQueryRepository}, {@link OrderQueryRepository}
 * : 화면 혹은 API 에 의존관계가 있는 로직들을 떼어서 모아둔 Repository
 * : 특정 화면이나 API 에 fit 한 query 들을 분리함, 특정화면 혹은 API 조회할 때 주로 사용
 * : 보통 화면에 종속적인 로직들이 쿼리와 밀접한 관련이 있을 경우가 많기 때문에, 이를 분리하면 유지보수 관리에 용이하다.
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
    public List<OrderQueryDto> findAllEachQuery() {
        List<OrderQueryDto> orders = findOrdersQuery();//query 1번 -> N 개
        orders.forEach(o -> {
            o.setOrderItems(
                    findOrderItemsEachQuery(o.getOrderId())//query N번
            );
        });
        return orders;
    }

    /**
     * 최적화
     * Query: 루트 1번, 컬렉션 1번
     * 데이터를 한꺼번에 처리할 때 많이 사용하는 방식
     */
    public List<OrderQueryDto> findAllInQuery() {
        List<OrderQueryDto> result = findOrdersQuery();//query 1번 -> N 개

        //orderItem 컬렉션을 MAP 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemsInQuery = findOrderItemsInQuery(getOrderIds(result));//query 1번 IN (N 개 key)

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
        result.forEach(o -> {
            o.setOrderItems(orderItemsInQuery.get(o.getOrderId()));
        });

        return result;
    }

    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private List<OrderQueryDto> findOrdersQuery() {
        return em.createQuery(
                        "select new com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from orders o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItemsEachQuery(Long orderId) {
        List<OrderItemQueryDto> result = em.createQuery(
                        "select new com.jpabook.jpashop.api.repository.query.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :id", OrderItemQueryDto.class)
                .setParameter("id", orderId)
                .getResultList();
        return result;
    }

    /**
     * orderQueryDto List -> orderId List
     */
    private List<Long> getOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    /**
     * 1:N 관계인 orderItems 조회
     * N번 조회 -> IN 조회조건으로 collection Table 1회 조회
     */
    private Map<Long, List<OrderItemQueryDto>> findOrderItemsInQuery(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new com.jpabook.jpashop.api.repository.query.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :ids", OrderItemQueryDto.class)
                .setParameter("ids", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemsGroupingByOrderId = orderItems
                .stream()
                .collect(Collectors
                        .groupingBy(OrderItemQueryDto::getOrderId)
                );

        return orderItemsGroupingByOrderId;
    }

    /**
     * 1:N:M 관계 데이터 전체 조회
     * 모든 관계된 테이블 1회 조회
     */
    public List<OrderFlatDto> findAllFlat() {
        List<OrderFlatDto> flats = em.createQuery(
                "select new com.jpabook.jpashop.api.repository.query.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from orders o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class).getResultList();
        return flats;
    }
}
