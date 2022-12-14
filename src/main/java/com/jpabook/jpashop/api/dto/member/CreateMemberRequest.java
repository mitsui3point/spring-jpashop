package com.jpabook.jpashop.api.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateMemberRequest {
    @NotEmpty
    private String name;

    @Builder
    private CreateMemberRequest(String name) {
        this.name = name;
    }
}
