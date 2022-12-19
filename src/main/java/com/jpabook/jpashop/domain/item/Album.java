package com.jpabook.jpashop.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "A")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {
    private String artist;
    private String etc;

    @Builder
    private Album(String name, int price, int stockQuantity, String artist, String etc) {
        super(name, price, stockQuantity);
        this.artist = artist;
        this.etc = etc;
    }
}
