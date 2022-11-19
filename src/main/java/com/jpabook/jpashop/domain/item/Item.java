package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.exception.NotEnoughItemStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Setter
    private String name;

    @Setter
    private int price;

    @Setter
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public void addStockQuantity(int addStockQuantity) {
        this.stockQuantity += addStockQuantity;
    }

    public void subtractStockQuantity(int subtractStockQuantity) {
        if (this.stockQuantity < subtractStockQuantity) {
            throw new NotEnoughItemStockException("수량이 부족합니다.");
        }
        this.stockQuantity -= subtractStockQuantity;
    }
}
