package com.jpabook.jpashop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.jpabook.jpashop.domain.constants.ExceptionMessage.ALREADY_EXISTS_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberApiControllerTest {

    private static final String BASE_URL = "/api";
    private static final String MEMBER_JOIN_V1_URL = BASE_URL + "/v1/members";
    private static final String MEMBER_JOIN_V2_URL = BASE_URL + "/v2/members";
    private static final String MEMBER_UPDATE_NAME_V2_URL = BASE_URL + "/v2/members/";
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager em;

    private Address address = Address.builder()
            .city("city")
            .street("street")
            .zipcode("zipcode")
            .build();
    private Member member = Member.builder()
            .name("member1")
            .address(address)
            .build();
    private Member noNameMember = Member.builder()
            .address(address)
            .build();
    private Member updateNameMember = Member.builder()
            .name("updateMember")
            .build();
    private Member updateNoNameMember = Member.builder()
            .build();

    @Test
    void 회원_가입_V1() throws Exception {
        memberJoin(MEMBER_JOIN_V1_URL);
    }
    @Test
    void 회원_가입_V2() throws Exception {
        memberJoin(MEMBER_JOIN_V2_URL);
    }

    @Test
    void 회원_가입_이름_중복_V1() throws JsonProcessingException {
        memberJoinDuplicateName(MEMBER_JOIN_V1_URL);
    }

    @Test
    void 회원_가입_이름_중복_V2() throws JsonProcessingException {
        memberJoinDuplicateName(MEMBER_JOIN_V2_URL);
    }

    @Test
    void 회원_가입_이름_누락_V1() throws Exception {
        memberJoinNoName(MEMBER_JOIN_V1_URL);
    }

    @Test
    void 회원_가입_이름_누락_V2() throws Exception {
        memberJoinNoName(MEMBER_JOIN_V2_URL);
    }

    @Test
    void 회원_수정_이름_V2() throws Exception {
        //given
        String updateNameBody = mapper.writeValueAsString(updateNameMember);

        //when
        memberService.join(member);
        ResultActions perform = mvc.perform(patch(MEMBER_UPDATE_NAME_V2_URL + member.getId())
                .content(updateNameBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        String expected = "{\"id\":1,\"name\":\"updateMember\"}";
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

    }

    @Test
    void 회원_수정_이름_누락_V2() throws Exception {
        //given
        String updateNoNameBody = mapper.writeValueAsString(updateNoNameMember);

        //when
        memberService.join(member);
        ResultActions performNoName = mvc.perform(patch(MEMBER_UPDATE_NAME_V2_URL + member.getId())
                .content(updateNoNameBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        performNoName.andDo(print())
                .andExpect(status().isBadRequest());

    }

    private void memberJoin(String url) throws Exception {
        //given
        String expectJson = "{\"id\":1}";

        //when
        String body = mapper.writeValueAsString(member);
        ResultActions perform = mvc.perform(post(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson));
    }

    private void memberJoinDuplicateName(String url) throws JsonProcessingException {
        //when
        String duplicateNameBody = mapper.writeValueAsString(member);
        memberService.join(member);

        //then
        Assertions.assertThatThrownBy(() -> {
            mvc.perform(post(url)
                    .content(duplicateNameBody)
                    .contentType(MediaType.APPLICATION_JSON)
            );
        }).hasCause(new IllegalStateException(ALREADY_EXISTS_NAME.getMessage()));
    }

    private void memberJoinNoName(String url) throws Exception {
        //when
        String noNameBody = mapper.writeValueAsString(noNameMember);
        ResultActions performNoName = mvc.perform(post(url)
                .content(noNameBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        performNoName.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    void tearDown() {
        em.createNativeQuery("alter sequence hibernate_sequence restart with 1")
                .executeUpdate();
        em.flush();
        em.clear();
    }
}
