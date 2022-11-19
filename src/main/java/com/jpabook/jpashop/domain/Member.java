package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Setter
    private String name;

    @Setter
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")//관계설정
    private List<Order> orders = new ArrayList<>();
}
