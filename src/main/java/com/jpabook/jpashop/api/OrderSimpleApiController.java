package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderService orderService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();//lazy 강제 초기화
            for (OrderItem orderItem : order.getOrderItems()) {
                orderItem.getOrderPrice();//lazy 강제 초기화;
                orderItem.getItem().getName();//lazy 강제 초기화
                for (Category category : orderItem.getItem().getCategories()) {
                    category.getName();//lazy 강제 초기화
                }
            }
            order.getDelivery().getDeliveryStatus();//lazy 강제 초기화
        }
        return orders;
    }
}