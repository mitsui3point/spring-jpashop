package com.jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm {
    private Long id;

    @NotEmpty(message = "상품 이름을 입력하세요.")
    private String name;

    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
