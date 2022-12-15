package com.jpabook.jpashop.api;

import com.jpabook.jpashop.api.dto.order.OrderApiDto;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.repository.order.query.dto.OrderFlatDto;
import com.jpabook.jpashop.repository.order.query.dto.OrderItemQueryDto;
import com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto;
import com.jpabook.jpashop.service.OrderService;
import com.jpabook.jpashop.service.order.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * <p>
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제
 * </p>
 * <p>
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 * </p>
 * <p>
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
 * - 페이징 시에는 N 부분을 포기해야함
 * </p>
 * <p>
 * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
 * - 페이징 가능
 * </p>
 * <p>
 * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
 * - 페이징 가능
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findAll(new OrderSearch());
        orders.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems()
                    .forEach(orderItem -> {
                        orderItem.getItem().getName();
                    });
        });
        return orders;
    }

    /**
     * V2. 엔티티를 DTO로 변환
     * 지연 로딩으로 너무 많은 SQL 실행
     */
    @GetMapping("/api/v2/orders")
    public List<OrderApiDto> ordersV2() {
        return orderService.findAll(new OrderSearch())
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 페이징 시에는 N 부분을 포기해야함
     */
    @GetMapping("/api/v3/orders")
    public List<OrderApiDto> ordersV3() {
        return orderService.findAllWithItem()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderApiDto> ordersV3_1(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "limit", defaultValue = "0") int limit) {
        return orderService.findPagingWithItem(offset, limit)
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }

    /**
     * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
     * - 페이징 가능
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryService.findAllEachQuery();
    }

    /**
     * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * - 페이징 가능
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryService.findAllInQuery();
    }

    /**
     * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
     * - 페이징 불가능..
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryService.findAllFlat();
        return flats.stream()
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
    }

}
