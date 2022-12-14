package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;

public class MemberTestDataField extends TestField {
    protected Member memberA;
    protected Member memberB;
    protected Member memberC;

    //===================//
    protected Address address;
    protected Member member;
    protected Member noNameMember;
    protected Member updateNameMember;
    protected Member updateNoNameMember;

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

        //===================//

        address = Address.builder()
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build();
        member = Member.builder()
                .name("member1")
                .address(address)
                .build();
        noNameMember = Member.builder()
                .address(address)
                .build();
        updateNameMember = Member.builder()
                .name("updateMember")
                .build();
        updateNoNameMember = Member.builder()
                .build();
    }
}
