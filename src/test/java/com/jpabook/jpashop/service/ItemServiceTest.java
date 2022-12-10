package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;
import org.junit.jupiter.api.AfterEach;
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
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    private final Item albumA = Album.builder()
            .name("albumA")
            .build();
    private final Item albumB = Album.builder()
            .name("albumB")
            .build();
    private final Item bookA = Book.builder()
            .name("bookA")
            .build();
    private final Item bookB = Book.builder()
            .name("bookB")
            .build();
    private final Item movieA = Movie.builder()
            .name("movieA")
            .build();
    private final Item movieB = Movie.builder()
            .name("movieA")
            .build();

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
