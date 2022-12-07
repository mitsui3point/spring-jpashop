package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import org.hibernate.collection.internal.PersistentBag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PersistentBagTest {
    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void memberPersistentBagTest() {
        //given
        Member member = Member.builder()
                .name("memberA")
                .build();
        System.out.println("member = " +
                member.getOrders()
                        .getClass()
        );

        //when
        entityManager.persist(member);
        List<Order> actual = member.getOrders();
        Class<PersistentBag> expected = PersistentBag.class;

        //then
        System.out.println("member = " +
                member.getOrders()
                        .getClass()
        );
        assertThat(actual).isInstanceOf(expected);
    }
}
