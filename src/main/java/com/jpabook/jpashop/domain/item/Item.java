package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.exception.NotEnoughItemStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {
    @Id
    @GeneratedValue
//    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Builder.Default
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    protected Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    //==연관관계 편의 메서드==//
    public void addStockQuantity(int addStockQuantity) {
        this.stockQuantity += addStockQuantity;
    }

    public void subtractStockQuantity(int subtractStockQuantity) {
        if (this.stockQuantity < subtractStockQuantity) {
            throw new NotEnoughItemStockException("수량이 부족합니다.");
        }
        this.stockQuantity -= subtractStockQuantity;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void changeCategories(List<Category> categories) {
        this.categories = categories;
    }
}
