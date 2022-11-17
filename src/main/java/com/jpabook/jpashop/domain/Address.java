package com.jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //@Embeddable jpa spec 상 요구하는 default constructor(리플렉션, ; Class 'Address' should have [public, protected] no-arg constructor; protected 로 막아두어 spec 을 맞추면서 사용을 제한하였다.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
