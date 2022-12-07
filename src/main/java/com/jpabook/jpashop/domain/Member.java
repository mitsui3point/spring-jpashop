package com.jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")//관계설정
    private List<Order> orders = new ArrayList<>();

    @Builder
    private Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    //==변경감지 메서드==//
    public void changeName(String name) {
        this.name = name;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }
}


