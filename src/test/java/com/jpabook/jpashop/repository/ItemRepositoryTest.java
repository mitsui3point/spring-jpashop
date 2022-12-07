package com.jpabook.jpashop.repository;

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
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @PersistenceContext
    private EntityManager em;

    private Item albumA = Album.builder()
            .name("albumA")
            .build();
    private Item albumB = Album.builder()
            .name("albumB")
            .build();
    private Item bookA = Book.builder()
            .name("bookA")
            .build();
    private Item bookB = Book.builder()
            .name("bookB")
            .build();
    private Item movieA = Movie.builder()
            .name("movieA")
            .build();
    private Item movieB = Movie.builder()
            .name("movieA")
            .build();

    @Test
    void 상품_데이터_등록() {
        //given
        Item expected = albumA;
        //when
        itemRepository.saveMerge(albumA);
        Item actual = itemRepository.findOne(albumA.getId());
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_데이터_수정() {
        //given
        String expected = "bookA";
        //when
        itemRepository.saveMerge(movieA);
        movieA.changeName(expected);
        itemRepository.saveMerge(movieA);
        Item actual = itemRepository.findOne(movieA.getId());
        //then
        assertThat(actual.getName()).isEqualTo(expected);
    }

    @Test
    void 상품_데이터_전체_조회() {
        //given
        List<Item> expected = Arrays.asList(new Item[]{albumA, albumB, bookA, bookB, movieA, movieB});
        //when
        for (Item item : expected) {
            itemRepository.saveMerge(item);
        }
        List<Item> actual = itemRepository.findAll();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
