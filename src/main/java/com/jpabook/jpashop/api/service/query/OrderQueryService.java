package com.jpabook.jpashop.api.service.query;

import com.jpabook.jpashop.api.repository.query.OrderQueryRepository;
import com.jpabook.jpashop.api.repository.query.dto.OrderFlatDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderItemQueryDto;
import com.jpabook.jpashop.api.repository.query.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public List<OrderQueryDto> findAllEachQuery() {
        return orderQueryRepository.findAllEachQuery();
    }

    public List<OrderQueryDto> findAllInQuery() {
        return orderQueryRepository.findAllInQuery();
    }

    public List<OrderFlatDto> findAllFlat() {
        return orderQueryRepository.findAllFlat();
    }

    public List<OrderQueryDto> findAllFlatApiV6() {
        return orderQueryRepository.findAllFlat()
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
    }
}
