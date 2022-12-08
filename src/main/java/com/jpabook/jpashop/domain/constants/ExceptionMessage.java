package com.jpabook.jpashop.domain.constants;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    ALREADY_EXISTS_NAME("중복된 이름이 존재합니다.");

    private String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }
}
