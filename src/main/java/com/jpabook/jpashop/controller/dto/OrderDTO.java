package com.jpabook.jpashop.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDTO {
    private Long memberId;
    private Long itemId;
    private int count;

    @Builder
    public OrderDTO(Long memberId, Long itemId, int count) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.count = count;
    }
}
