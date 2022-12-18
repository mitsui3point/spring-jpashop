package com.jpabook.jpashop.api.service.simplequery;

import com.jpabook.jpashop.api.repository.simplequery.OrderSimpleQueryRepository;
import com.jpabook.jpashop.api.repository.simplequery.dto.OrderSimpleQueryDto;
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
