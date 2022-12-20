package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.MemberTestDataField;
import com.jpabook.jpashop.domain.Member;
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
public class MemberRepositoryTest extends MemberTestDataField {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 회원_데이터_등록_조회() {
        //given
        Member joinMember = memberA;
        //when
        memberRepository.save(joinMember);
        Member joinedMember = memberRepository.findById(joinMember.getId()).get();
        //then
        assertThat(joinMember).isEqualTo(joinedMember);
    }

    @Test
    void 회원_데이터_전체_조회() {
        //given
        List<Member> expected = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        expected.addAll(Arrays.asList(memberA, memberB, memberC));
        //when
        List<Member> actual = memberRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected.toArray(Member[]::new));
    }

    @Test
    void 회원_데이터_이름조건_조회() {
        //given
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        List<Member> expected = Arrays.asList(memberA);
        //when
        List<Member> actual = memberRepository.findByName("memberA");
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    void tearDown() {
        em.flush();
    }
}
