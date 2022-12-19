package com.jpabook.jpashop.api.service.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.MemberTestDataField;
import com.jpabook.jpashop.api.member.*;
import com.jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static com.jpabook.jpashop.domain.constants.ExceptionMessage.ALREADY_EXISTS_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class MemberQueryServiceTest extends MemberTestDataField {
    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    private EntityManager em;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 회원_가입_V1() throws JsonProcessingException {
        //when
        CreateMemberResponse apiResponse = memberQueryService.saveMemberV1(member);
        String expected = mapper.writeValueAsString(member);

        String actual = mapper.writeValueAsString(em.find(Member.class, apiResponse.getId()));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 회원_가입_V2() {
        //when
        CreateMemberRequest apiRequest = CreateMemberRequest.builder()
                .name(member.getName())
                .build();
        CreateMemberResponse apiResponse = memberQueryService.saveMemberV2(apiRequest);
        String expected = member.getName();

        String actual = em.find(Member.class, apiResponse.getId()).getName();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 회원_가입_이름_중복_V1() {
        //given
        memberQueryService.saveMemberV1(member);

        //then
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
            //when
            memberQueryService.saveMemberV1(member);
        }).withMessageContaining(ALREADY_EXISTS_NAME.getMessage());
    }

    @Test
    void 회원_가입_이름_중복_V2() {
        //given
        memberQueryService.saveMemberV2(CreateMemberRequest.builder()
                .name(member.getName())
                .build());

        //then
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
            //when
            memberQueryService.saveMemberV2(CreateMemberRequest.builder()
                    .name(member.getName())
                    .build());
        }).withMessageContaining(ALREADY_EXISTS_NAME.getMessage());
    }

    @Test
    void 회원_가입_이름_누락_V1() {
        //when
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            memberQueryService.saveMemberV1(noNameMember);
        });
    }

    @Test
    void 회원_가입_이름_누락_V2() {
        //when
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            memberQueryService.saveMemberV2(
                    CreateMemberRequest.builder()
                            .name(noNameMember.getName())
                            .build());
        });
    }

    @Test
    void 회원_수정_이름_V2() {
        //given
        Long id = memberQueryService.saveMemberV2(
                        CreateMemberRequest.builder()
                                .name("name")
                                .build())
                .getId();

        String expected = "updateName";
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .name(expected)
                .build();

        //when
        UpdateMemberResponse response = memberQueryService.updateMemberV2(id, request);
        String actual = response.getName();

        //then
        assertThat(id).isEqualTo(response.getId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 회원_수정_이름_누락_V2() {
        //given
        Long id = memberQueryService.saveMemberV2(
                        CreateMemberRequest.builder()
                                .name("name")
                                .build())
                .getId();

        String expected = "";
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .name(expected)
                .build();

        //then
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            //when
            UpdateMemberResponse response = memberQueryService.updateMemberV2(id, request);
        });
    }

    @Test
    @Transactional(readOnly = true)
    void 회원_전체조회_V1() {
        //given
        List<Member> expected = em.createQuery("select m from Member m", Member.class).getResultList();
        //when
        List<Member> actual = memberQueryService.getMembersV1();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional(readOnly = true)
    void 회원_전체조회_V2() throws JsonProcessingException {
        //given
        List<MemberDto> data = em.createQuery("select m from Member m", Member.class)
                .getResultList()
                .stream()
                .map(member -> MemberDto
                        .builder()
                        .name(member.getName())
                        .build())
                .collect(Collectors.toList());
        Results expected = Results.builder()
                .data(data)
                .count(data.size())
                .build();
        String expectedJson = mapper.writeValueAsString(expected);
        //when
        Results actual = memberQueryService.getMembersV2();
        String actualJson = mapper.writeValueAsString(actual);
        //then
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}
