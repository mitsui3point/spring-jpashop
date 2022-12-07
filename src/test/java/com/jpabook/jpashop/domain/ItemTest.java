package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.exception.NotEnoughItemStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
public class ItemTest {

    private int addStockQuantity = 4;
    private int subtractStockQuantity = 3;
    private int subtractBiggerThanStockQuantity = 5;

    @Test
    void 상품_재고_add() {
        //given
        Item item = Album.builder().build();
        item.addStockQuantity(addStockQuantity);
        int expected = addStockQuantity;
        //when
        int actual = item.getStockQuantity();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_재고_subtract() {
        //given
        Item item = Album.builder().build();
        item.addStockQuantity(addStockQuantity);
        int expected = addStockQuantity - subtractStockQuantity;
        //when
        item.subtractStockQuantity(subtractStockQuantity);
        int actual = item.getStockQuantity();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_현재고_마이너스_예외() {
        //given
        Item item = Album.builder().build();
        item.addStockQuantity(addStockQuantity);
        //then
        Throwable actual = Assertions.assertThrows(NotEnoughItemStockException.class, () -> {
            //when
            item.subtractStockQuantity(subtractBiggerThanStockQuantity);
        });
    }
}
