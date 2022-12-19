package com.jpabook.jpashop.api.service.osiv;

import com.jpabook.jpashop.api.order.SimpleOrderDto;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSimpleOpenInViewService {

    private final OrderRepository orderRepository;
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        orders.forEach(order -> {
            order.getMember().getName();//lazy 강제 초기화
            order.getDelivery().getDeliveryStatus();//lazy 강제 초기화
            order.getOrderItems().forEach(orderItem -> {
                orderItem.getItem().getName();//lazy 강제 초기화
            });
        });
        return orders;
    }

    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N + 배송 N
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(order ->
                        SimpleOrderDto.builder()
                                .order(order)
                                .build()
                )
                .collect(Collectors.toList());
    }

    public List<SimpleOrderDto> ordersV3() {
        return orderRepository.findAllWithMemberDelivery()
                .stream()
                .map(order ->
                        SimpleOrderDto.builder()
                                .order(order)
                                .build()
                )
                .collect(Collectors.toList());
    }
}
