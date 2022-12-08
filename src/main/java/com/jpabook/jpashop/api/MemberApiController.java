package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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
                .name(request.name)
                .build();
        Long id = memberService.join(member);
        return CreateMemberResponse.builder()
                .id(id)
                .build();
    }

    /**
     * API parameter DTO(parameter)
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class CreateMemberResponse {
        private Long id;

        @Builder
        private CreateMemberResponse(Long id) {
            this.id = id;
        }

    }

    /**
     * API parameter DTO(return)
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class CreateMemberRequest {
        @NotEmpty
        private String name;

        @Builder
        private CreateMemberRequest(String name) {
            this.name = name;
        }
    }
}
