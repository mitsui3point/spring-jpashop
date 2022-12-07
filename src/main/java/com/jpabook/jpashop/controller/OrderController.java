package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.controller.dto.OrderDTO;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.service.ItemService;
import com.jpabook.jpashop.service.MemberService;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findAll();
        List<Item> items = itemService.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "orders/createOrderForm";
    }

    @PostMapping("/order")
    public String createOrder(@ModelAttribute OrderDTO orderDTO) {
        try {
            orderService.order(
                    orderDTO.getMemberId(),
                    orderDTO.getItemId(),
                    orderDTO.getCount()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/order";
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orders(@ModelAttribute OrderSearch orderSearch,
                         Model model) {
        List<Order> orders = orderRepository.findAllByCriteria(orderSearch);
        model.addAttribute("orderSearch", orderSearch);
        model.addAttribute("orders", orders);
        return "orders/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String orderCancel(@PathVariable(value = "orderId") Long orderId,
                              Model model) {
        try {
            orderService.cancel(orderId);
        } catch (Exception e) {
            return "orders/orderList";
        }
        return "redirect:/orders";
    }
}
