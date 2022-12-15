package com.jpabook.jpashop.service.order.query;

import com.jpabook.jpashop.repository.order.query.OrderQueryRepository;
import com.jpabook.jpashop.repository.order.query.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
