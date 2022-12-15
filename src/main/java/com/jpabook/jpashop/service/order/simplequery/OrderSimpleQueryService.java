package com.jpabook.jpashop.service.order.simplequery;

import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import com.jpabook.jpashop.repository.order.simplequery.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSimpleQueryService {

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
