package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager em;

    private final Member memberA = Member.builder()
            .name("memberA")
            .build();
    private final Member memberB = Member.builder()
            .name("memberB")
            .build();
    private final Member memberC = Member.builder()
            .name("memberC")
            .build();

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
        Member duplicateNameMemberA = Member.builder().name("memberA").build();
        duplicateNameMemberA.changeName("memberA");
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
        List<Member> expected = em.createQuery("select m from Member m")
                .getResultList();
        memberService.join(memberA);
        memberService.join(memberB);
        memberService.join(memberC);
        expected.addAll(Arrays.asList(memberA, memberB, memberC));

        //when
        List<Member> actual = memberService.findAll();
        //then
        assertThat(actual).containsExactly(expected.toArray(Member[]::new));
    }

    @Test
    void 회원이름수정() {
        //given
        String expectedName = "changeMemberName";
        Long memberId = memberService.join(memberA);

        //when
        memberService.updateName(memberId, expectedName);
        String actualName = memberService.findOne(memberId)
                .getName();

        //then
        assertThat(actualName).isEqualTo(expectedName);
    }

    @AfterEach
    void tearDown() {
        em.flush(); //PersistenceContext 변경사항 DB에 반영
    }
}
