package com.jpabook.jpashop.api.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateMemberResponse {
    private Long id;

    @Builder
    private CreateMemberResponse(Long id) {
        this.id = id;
    }

}