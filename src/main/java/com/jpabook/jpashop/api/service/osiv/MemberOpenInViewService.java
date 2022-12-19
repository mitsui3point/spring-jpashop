package com.jpabook.jpashop.api.service.osiv;

import com.jpabook.jpashop.api.member.*;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.jpabook.jpashop.domain.constants.ExceptionMessage.ALREADY_EXISTS_NAME;

/**
 * {@code : @Validated}<p/>
 * 입력 파라미터의 유효성 검증은 컨트롤러에서 최대한 처리하고 넘겨주는 것이 좋다.<br/>
 * 하지만 개발을 하다보면 불가피하게 다른 곳에서 파라미터를 검증해야 할 수 있다.<br/>
 * Spring에서는 이를 위해 AOP 기반으로 메소드의 요청을 가로채서 유효성 검증을 진행해주는 {@code @Validated}를 제공하고 있다.<br/>
 * {@code @Validated}는 JSR 표준 기술이 아니며,<br/>
 * Spring 프레임워크에서 제공하는 어노테이션 및 기능이다.<br/>
 * {@linkplain : https://mangkyu.tistory.com/174}<br/>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class MemberOpenInViewService {

    private final MemberRepository memberRepository;

    @Transactional
    public CreateMemberResponse saveMemberV1(@Valid Member member) {
        validateDuplicateMemberName(member);
        memberRepository.save(member);
        return CreateMemberResponse.builder()
                .id(member.getId())
                .build();
    }

    @Transactional
    public CreateMemberResponse saveMemberV2(@Valid CreateMemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .build();
        validateDuplicateMemberName(member);
        memberRepository.save(member);
        return CreateMemberResponse.builder()
                .id(member.getId())
                .build();
    }

    private void validateDuplicateMemberName(Member member) {
        boolean isExistDuplicateMember = !memberRepository.findByName(member.getName())
                .isEmpty();
        if (isExistDuplicateMember) {
            throw new IllegalStateException(ALREADY_EXISTS_NAME.getMessage());
        }
    }

    @Transactional
    public UpdateMemberResponse updateMemberV2(Long id, @Valid UpdateMemberRequest request) {
        Member member = memberRepository.findOne(id);
        member.changeName(request.getName());

        return UpdateMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }

    public List<Member> getMembersV1() {
        return memberRepository.findAll();
    }

    public Results getMembersV2() {
        List<MemberDto> data = memberRepository.findAll()
                .stream()
                .map(member -> MemberDto
                        .builder()
                        .name(member.getName())
                        .build())
                .collect(Collectors.toList());
        return Results.builder()
                .data(data)
                .count(data.size())
                .build();
    }
}
