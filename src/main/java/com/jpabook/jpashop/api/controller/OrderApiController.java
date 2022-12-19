package com.jpabook.jpashop.api.controller;

import com.jpabook.jpashop.api.order.OrderApiDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import com.jpabook.jpashop.api.service.osiv.OrderOpenInViewService;
import com.jpabook.jpashop.api.service.query.OrderQueryService;
import com.jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private final OrderOpenInViewService orderOpenInViewService;
    private final OrderQueryService orderQueryService;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        return orderOpenInViewService.ordersV1();
    }

    /**
     * V2. 엔티티를 DTO로 변환
     * 지연 로딩으로 너무 많은 SQL 실행
     */
    @GetMapping("/api/v2/orders")
    public List<OrderApiDto> ordersV2() {
        return orderOpenInViewService.ordersV2();
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 페이징 시에는 N 부분을 포기해야함
     */
    @GetMapping("/api/v3/orders")
    public List<OrderApiDto> ordersV3() {
        return orderOpenInViewService.ordersV3();
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderApiDto> ordersV3_1(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "limit", defaultValue = "0") int limit) {
        return orderOpenInViewService.ordersV3_1(offset, limit);
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
        return orderQueryService.findAllFlatApiV6();
    }

}
