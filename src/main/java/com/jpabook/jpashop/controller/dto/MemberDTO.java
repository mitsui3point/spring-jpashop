package com.jpabook.jpashop.controller.dto;

import com.jpabook.jpashop.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDTO {
    private Long id;
    private String name;
    private Address address;
}
