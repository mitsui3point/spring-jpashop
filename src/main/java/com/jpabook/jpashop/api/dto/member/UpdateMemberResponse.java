package com.jpabook.jpashop.api.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UpdateMemberResponse {
    private Long id;
    private String name;

    @Builder
    private UpdateMemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
