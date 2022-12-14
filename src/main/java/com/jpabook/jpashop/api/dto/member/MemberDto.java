package com.jpabook.jpashop.api.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberDto {
    private String name;

    @Builder
    private MemberDto(String name) {
        this.name = name;
    }
}
