package com.jpabook.jpashop.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long memberId;
    private Long itemId;
    private int count;
}
