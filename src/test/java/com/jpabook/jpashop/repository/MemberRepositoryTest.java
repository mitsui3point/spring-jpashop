package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

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
    void 회원_데이터_등록_조회() {
        //given
        Member joinMember = memberA;
        //when
        memberRepository.save(joinMember);
        Member joinedMember = memberRepository.findOne(joinMember.getId());
        //then
        assertThat(joinMember).isEqualTo(joinedMember);
    }

    @Test
    void 회원_데이터_전체_조회() {
        //given
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        Member[] expected = new Member[]{memberC, memberB, memberA};
        //when
        List<Member> actual = memberRepository.findAll();
        //then
        assertThat(actual).containsOnly(expected);
    }

    @Test
    void 회원_데이터_이름조건_조회() {
        //given
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        List<Member> expected = new ArrayList<>(Arrays.asList(memberA));
        //when
        List<Member> actual = memberRepository.findByName("memberA");
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
