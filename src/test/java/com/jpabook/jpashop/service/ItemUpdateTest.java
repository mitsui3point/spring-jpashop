package com.jpabook.jpashop.service;

import com.jpabook.jpashop.controller.BookForm;
import com.jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemUpdateTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ItemService itemService;

    @Test
    void itemUpdateUsingMergeTest() {
        //given
        Book item1 = new Book();
        item1.setName("item");
        item1.setAuthor("author1");
        //when
        Book actualInsert = (Book) itemService.findOne(itemService.saveItemMerge(item1));
        em.flush();
        //then
        assertThat(actualInsert.getName())
                .isEqualTo("item");
        assertThat(actualInsert.getAuthor())
                .isEqualTo("author1");

        //given
        Book item2 = new Book();
        item2.setId(item1.getId());
        item2.setName("item2");
        //when
        Book actualUpdate = (Book) itemService.findOne(itemService.saveItemMerge(item2));
        em.flush();
        //then
        assertThat(actualUpdate.getName())
                .as("update 대상")
                .isEqualTo("item2");
        assertThat(actualUpdate.getAuthor())
                .as(new StringBuilder("update 대상 아님")
                        .append("\n\t")
                        .append("entityManager.merge() 는 엔티티 내 모든 필드를 update 처리")
                        .append("\n\t")
                        .append("update 대상이 아닌 author 필드를 null update 처리")
                        .toString())
                .isNull();
    }

    @Test
    void itemUpdateUsingDirtyCheckingTest() {
        //given
        BookForm bookForm1 = new BookForm();
        bookForm1.setName("item");
        bookForm1.setAuthor("author1");
        //when
        Book actualInsert = (Book) itemService.findOne(itemService.saveItem(bookForm1));
        em.flush();
        //then
        assertThat(actualInsert.getName())
                .isEqualTo("item");
        assertThat(actualInsert.getAuthor())
                .isEqualTo("author1");

        //given
        BookForm bookForm2 = new BookForm();
        bookForm2.setId(actualInsert.getId());
        bookForm2.setName("item2");
        bookForm2.setAuthor("author1");
        //when
        Book actualUpdate = (Book) itemService.findOne(itemService.saveItem(bookForm2));
        em.flush();
        //then
        assertThat(actualUpdate.getName())
                .as("update 대상")
                .isEqualTo("item2");
        assertThat(actualUpdate.getAuthor())
                .as(new StringBuilder("update 대상 아님")
                        .append("\n\t")
                        .append("dirty checking 은 엔티티 중 필요한 필드만 update 처리 가능")
                        .append("\n\t")
                        .append("update 대상이 아닌 author 의 기존 value(author1) 를 그대로 유지")
                        .toString())
                .isEqualTo("author1");
    }
}
