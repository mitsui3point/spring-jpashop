package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;

public class ItemTestDataField extends TestField {
    protected int addStockQuantity;
    protected int subtractStockQuantity;
    protected int subtractBiggerThanStockQuantity;
    protected Item albumA;
    protected Item albumB;
    protected Item bookA;
    protected Item bookB;
    protected Item movieA;
    protected Item movieB;

    protected void init() {
        addStockQuantity = 4;
        subtractStockQuantity = 3;
        subtractBiggerThanStockQuantity = 5;
        albumA = Album.builder()
                .name("albumA")
                .build();
        albumB = Album.builder()
                .name("albumB")
                .build();
        bookA = Book.builder()
                .name("bookA")
                .build();
        bookB = Book.builder()
                .name("bookB")
                .build();
        movieA = Movie.builder()
                .name("movieA")
                .build();
        movieB = Movie.builder()
                .name("movieA")
                .build();
    }
}
