package com.jpabook.jpashop.domain.item;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "B")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String author;

    private String isbn;

    @Builder
    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    //==변경감지 메서드==//

    public void changeAuthor(String author) {
        this.author = author;
    }

    public void changeIsbn(String isbn) {
        this.isbn = isbn;
    }
}
