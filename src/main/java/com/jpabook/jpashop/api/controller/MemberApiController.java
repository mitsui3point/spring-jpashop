package com.jpabook.jpashop.api.controller;

import com.jpabook.jpashop.api.member.*;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MemberApiController
 * <p>
 * {@code @RestController: @Controller + @ResponseBody}
 * </p>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.<br />
     * 문제점<br />
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.<br />
     * - 기본적으로 엔티티의 모든 값이 노출된다.<br />
     * - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)<br />
     * - 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.<br />
     * - 엔티티가 변경되면 API 스펙이 변한다.<br />
     * - 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)<br />
     * 결론<br />
     * - API 응답 스펙에 맞추어 별도의 DTO를 반환한다.<br /><br />
     * 조회 V1: 안 좋은 버전, 모든 엔티티가 노출, @JsonIgnore -> 이건 정말 최악,<br />
     * api가 이거 하나인가! 화면에 종속적이지 마라!<br />
     */
    @GetMapping("/v1/members")
    public List<Member> getMembersV1() {
        return memberService.findAll();
    }

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다.<br />
     * - 엔티티가 변경이 되어도 API 스펙을 변경하지 않아도 된다.<br />
     */
    @GetMapping("/v2/members")
    public Results getMembersV2() {
        List<MemberDto> data = memberService.findAll()
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

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.<br />
     * 문제점<br />
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.<br />
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)<br />
     * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다.<br />
     * - 엔티티가 변경되면 API 스펙이 변한다.<br />
     * 결론<br />
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.<br />
     */
    @PostMapping("/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return CreateMemberResponse.builder()
                .id(id)
                .build();
    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .build();
        Long id = memberService.join(member);
        return CreateMemberResponse.builder()
                .id(id)
                .build();
    }

    @PatchMapping("/v2/members/{id}")//부분수정 Patch, 전체수정 Put
    public UpdateMemberResponse updateMemberV2(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.updateName(id, request.getName());
        Member member = memberService.findOne(id);

        return UpdateMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }
}
