package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager em;

    private Member memberA;
    private Member memberB;
    private Member memberC;

    @BeforeEach
    void setUp() {
        memberA = new Member();
        memberA.setName("memberA");

        memberB = new Member();
        memberB.setName("memberB");

        memberC = new Member();
        memberC.setName("memberC");
    }

    @Test
    void 회원가입() {
        //given
        Member expected = memberA;
        //when
        Long joinId = memberService.join(memberA);
        Member actual = memberService.findOne(joinId);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void 중복회원가입() {
        //given
        Member duplicateNameMemberA = new Member();
        duplicateNameMemberA.setName("memberA");
        //when
        memberService.join(memberA);
        try {
            memberService.join(duplicateNameMemberA);
        } catch (IllegalStateException illegalStateException) {
            return;
        }
        //then
        Assertions.fail("예외가 발생해야 한다."); //도달하면 안되는 지점
    }

    @Test
    void 회원전체조회() {
        //given
        memberService.join(memberA);
        memberService.join(memberB);
        memberService.join(memberC);
        //when
        List<Member> actual = memberService.findAll();
        //then
        assertThat(actual).containsOnly(memberA, memberB, memberC);
    }

    @AfterEach
    void tearDown() {
        em.flush(); //PersistenceContext 변경사항 DB에 반영
    }
}
