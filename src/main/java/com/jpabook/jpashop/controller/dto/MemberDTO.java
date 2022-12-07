package com.jpabook.jpashop.controller.dto;

import com.jpabook.jpashop.domain.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDTO {
    private Long id;
    private String name;
    private Address address;

    @Builder
    private MemberDTO(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
