package com.jpabook.jpashop.api.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Results<T> {
    private T data;
    private int count;

    @Builder
    private Results(T data, int count) {
        this.data = data;
        this.count = count;
    }
}
