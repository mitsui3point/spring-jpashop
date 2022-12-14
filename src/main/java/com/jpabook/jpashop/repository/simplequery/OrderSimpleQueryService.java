package com.jpabook.jpashop.repository.simplequery;

import com.jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto;
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
