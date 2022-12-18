package com.jpabook.jpashop.api.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UpdateMemberRequest {
    @NotEmpty
    private String name;

    @Builder
    private UpdateMemberRequest(String name) {
        this.name = name;
    }
}