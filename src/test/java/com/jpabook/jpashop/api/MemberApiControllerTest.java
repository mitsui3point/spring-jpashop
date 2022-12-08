package com.jpabook.jpashop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)//각 메서드마다 applicationContext 가 새로 생성되어 테스트 실행 속도가 매우 느리다.
public class MemberApiControllerTest {

    private static final String BASE_URL = "/api/v1";
    private static final String MEMBER_JOIN_URL = BASE_URL + "/members";
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;


    @BeforeEach
    void setUp() {
    }

    @Test
    @Transactional
    void 회원_가입() throws Exception {
        //given
        Address address = Address.builder()
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build();
        Member member = Member.builder()
                .name("member1")
                .address(address)
                .build();

        //when
        String body = mapper.writeValueAsString(member);
        ResultActions perform = mvc.perform(
                post(MEMBER_JOIN_URL)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        String expectJson = "{\"id\":1}";
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson));
    }

    @Test
    @Transactional
    void 회원_가입_이름_중복() throws JsonProcessingException {
        //given
        Address address = Address.builder()
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build();
        Member member = Member.builder()
                .name("member1")
                .address(address)
                .build();

        //when
        String duplicateNameBody = mapper.writeValueAsString(member);
        memberRepository.save(member);
        //then
        Assertions.assertThatThrownBy(() -> {
            mvc.perform(
                    post(MEMBER_JOIN_URL)
                            .content(duplicateNameBody)
                            .contentType(MediaType.APPLICATION_JSON)
            );
        }).hasCause(new IllegalStateException(ALREADY_EXISTS_NAME.getMessage()));
    }

    @Test
    @Transactional
    void 회원_가입_이름_누락() throws Exception {
        //given
        Address address = Address.builder()
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build();
        Member noNameMember = Member.builder()
                .address(address)
                .build();

        //when
        String noNameBody = mapper.writeValueAsString(noNameMember);
        ResultActions performNoName = mvc.perform(
                post(MEMBER_JOIN_URL)
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
