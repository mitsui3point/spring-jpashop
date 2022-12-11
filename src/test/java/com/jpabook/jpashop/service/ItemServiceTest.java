package com.jpabook.jpashop.service;

import com.jpabook.jpashop.ItemTestDataField;
import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemServiceTest extends ItemTestDataField {
    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    public void 상품저장() {
        //given
        Item expected = albumA;
        //when
        Long saveItemId = itemService.saveItemMerge(albumA);
        Item actual = itemService.findOne(saveItemId);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품수정() {
        //given
        String expected = "changeItemName";
        //when
        itemService.saveItemMerge(albumA);
        albumA.changeName(expected);
        Long mergeItemId = itemService.saveItemMerge(albumA);
        Item actual = itemService.findOne(mergeItemId);
        //then
        assertThat(actual.getName()).isEqualTo(expected);
    }

    @Test
    void 상품전체조회() {
        //given
        List<Item> expected = em.createQuery("select b from Book b").getResultList();
        expected.addAll(Arrays.asList(albumA, albumB, bookA, bookB, movieA, movieB));
        //when
        for (Item item : expected) {
            itemService.saveItemMerge(item);
        }
        List<Item> actual = itemService.findItems();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
