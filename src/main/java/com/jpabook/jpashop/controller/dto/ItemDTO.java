package com.jpabook.jpashop.controller.dto;

import com.jpabook.jpashop.controller.form.BookForm;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemDTO {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;


    private BookForm bookForm;

    @Builder
    private ItemDTO(BookForm bookForm) {
        this.bookForm = bookForm;
        this.id = this.bookForm.getId();
        this.name = this.bookForm.getName();
        this.price = this.bookForm.getPrice();
        this.stockQuantity = this.bookForm.getStockQuantity();
        this.author = this.bookForm.getAuthor();
        this.isbn = this.bookForm.getIsbn();
    }

    public static ItemDTO getBookDTO(BookForm bookForm) {
        return ItemDTO.builder()
                .bookForm(bookForm)
                .build();
    }
}
