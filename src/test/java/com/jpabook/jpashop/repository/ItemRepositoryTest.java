package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.ItemTestDataField;
import com.jpabook.jpashop.domain.item.Item;
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
public class ItemRepositoryTest extends ItemTestDataField {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private EntityManager em;
    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 상품_데이터_등록() {
        //given
        Item expected = albumA;
        //when
        itemRepository.save(albumA);
        Item actual = itemRepository.findOne(albumA.getId());
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_데이터_수정() {
        //given
        String expected = "bookA";
        //when
        itemRepository.save(movieA);
        movieA.changeName(expected);
        itemRepository.save(movieA);
        Item actual = itemRepository.findOne(movieA.getId());
        //then
        assertThat(actual.getName()).isEqualTo(expected);
    }

    @Test
    void 상품_데이터_전체_조회() {
        //given
        List<Item> expected = em.createQuery("select b from Book b").getResultList();
        List<Item> addItems = Arrays.asList(albumA, albumB, bookA, bookB, movieA, movieB);
        expected.addAll(addItems);
        //when
        for (Item item : addItems) {
            itemRepository.save(item);
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
