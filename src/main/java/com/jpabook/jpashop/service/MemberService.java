package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMemberName(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMemberName(Member member) {
        boolean isExistDuplicateMember = !memberRepository.findByName(member.getName()).isEmpty();
        if (isExistDuplicateMember) {
            throw new IllegalStateException("중복된 이름이 존재합니다.");
        }
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
