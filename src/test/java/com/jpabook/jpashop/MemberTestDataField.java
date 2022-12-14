package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Member;

public class MemberTestDataField extends TestField {
    protected Member memberA;
    protected Member memberB;
    protected Member memberC;
    protected void init() {
        memberA = Member.builder()
                .name("memberA")
                .build();
        memberB = Member.builder()
                .name("memberB")
                .build();
        memberC = Member.builder()
                .name("memberC")
                .build();
    }
}
