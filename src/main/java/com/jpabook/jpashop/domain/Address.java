package com.jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//@Embeddable jpa spec 상 요구하는 default constructor(리플렉션, ; Class 'Address' should have [public, protected] no-arg constructor; protected 로 막아두어 spec 을 맞추면서 사용을 제한하였다.
public class Address {
    private String city;
    private String street;
    private String zipcode;

    @Builder
    private Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
