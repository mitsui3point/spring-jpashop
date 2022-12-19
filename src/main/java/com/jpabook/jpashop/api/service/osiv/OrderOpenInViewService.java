package com.jpabook.jpashop.api.service.osiv;

import com.jpabook.jpashop.api.order.OrderApiDto;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderOpenInViewService {
    private final OrderRepository orderRepository;

    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
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

    public List<OrderApiDto> ordersV2() {
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }

    public List<OrderApiDto> ordersV3() {
        return orderRepository.findAllWithItem()
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }

    public List<OrderApiDto> ordersV3_1(int offset, int limit) {
        return orderRepository.findPagingWithItem(offset, limit)
                .stream()
                .map(order -> OrderApiDto.builder()
                        .order(order)
                        .build()
                )
                .collect(toList());
    }
}
