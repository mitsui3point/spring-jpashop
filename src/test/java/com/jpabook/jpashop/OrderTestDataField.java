package com.jpabook.jpashop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTestDataField extends TestField {
    protected String member1Name;
    protected String book1Name;
    protected int book1StockQuantity;
    protected int book1Price;
    protected int book1OrderCount;
    protected int book1OrderPrice;
    protected String book2Name;
    protected int book2StockQuantity;
    protected int book2Price;
    protected int book2OrderCount;
    protected int book2OrderPrice;
    protected Address address1;
    protected Member member1;
    protected Delivery delivery1;
    protected Book book1;
    protected Book book2;
    protected OrderStatus orderStatus1;
    protected OrderItem orderItem1;
    protected OrderItem orderItem2;
    protected OrderItem[] orderItems1;

    //===============================================//

    protected String member2Name;
    protected String changeMember2Name;
    protected String book3Name;
    protected int book3StockQuantity;
    protected int book3Price;
    protected int book3OrderCount;
    protected int book3OrderPrice;
    protected String book4Name;
    protected int book4StockQuantity;
    protected int book4Price;
    protected int book4OrderCount;
    protected int book4OrderPrice;
    protected Address address2;
    protected Member member2;
    protected Delivery delivery2;
    protected Book book3;
    protected Book book4;
    protected OrderStatus orderStatus2;
    protected OrderItem orderItem3;
    protected OrderItem orderItem4;
    protected OrderItem[] orderItems2;

    /**
     * https://itpro.tistory.com/117
     */
    protected ObjectMapper mapper;

    protected void init() {
        member1Name = "member1";

        book1Name = "jpa book";
        book1StockQuantity = 10;
        book1Price = 10000;
        book1OrderPrice = book1Price;
        book1OrderCount = 9;

        book2Name = "jpa book2";
        book2StockQuantity = 299;
        book2Price = 10000;
        book2OrderPrice = book2Price;
        book2OrderCount = 290;

        address1 = Address.builder()
                .city("서울")
                .street("스트릿")
                .zipcode("123-123")
                .build();
        member1 = Member.builder()
                .name(member1Name)
                .address(address1)
                .build();
        delivery1 = Delivery.builder()
                .address(address1)
                .build();
        orderStatus1 = OrderStatus.ORDER;
        book1 = Book.builder()
                .name(book1Name)
                .stockQuantity(book1StockQuantity)
                .price(book1Price)
                .build();
        book2 = Book.builder()
                .name(book2Name)
                .stockQuantity(book2StockQuantity)
                .price(book2Price)
                .build();
        orderItem1 = OrderItem.builder()
                .item(book1)
                .orderPrice(book1OrderPrice)
                .count(book1OrderCount)
                .build();
        orderItem2 = OrderItem.builder()
                .item(book2)
                .orderPrice(book2OrderPrice)
                .count(book2OrderCount)
                .build();
        orderItems1 = new OrderItem[]{orderItem1, orderItem2};

        member2Name = "member2";
        changeMember2Name = "mem2";

        book3Name = "jpa book3";
        book3StockQuantity = 50;
        book3Price = 10000;
        book3OrderPrice = 12000;
        book3OrderCount = 40;

        book4Name = "jpa book4";
        book4StockQuantity = 60;
        book4Price = 10500;
        book4OrderPrice = 13000;
        book4OrderCount = 50;

        address2 = Address.builder()
                .city("서울")
                .street("스트릿")
                .zipcode("123-123")
                .build();
        member2 = Member.builder()
                .name(member2Name)
                .address(address2)
                .build();
        delivery2 = Delivery.builder()
                .address(address2)
                .build();
        orderStatus2 = OrderStatus.ORDER;
        book3 = Book.builder()
                .name(book3Name)
                .stockQuantity(book3StockQuantity)
                .price(book3Price)
                .build();
        book4 = Book.builder()
                .name(book4Name)
                .stockQuantity(book4StockQuantity)
                .price(book4Price)
                .build();
        orderItem3 = OrderItem.builder()
                .item(book3)
                .orderPrice(book3OrderPrice)
                .count(book3OrderCount)
                .build();
        orderItem4 = OrderItem.builder()
                .item(book4)
                .orderPrice(book4OrderPrice)
                .count(book4OrderCount)
                .build();
        orderItems2 = new OrderItem[]{orderItem3, orderItem4};

        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    protected Long persistMember(String name, Address address, EntityManager em) {
        Member member = Member.builder()
                .name(name)
                .address(address)
                .build();

        em.persist(member);

        return member.getId();
    }

    protected Long persistBookItem(String name, int stockQuantity, int price, EntityManager em) {
        Item book = Book.builder()
                .name(name)
                .stockQuantity(stockQuantity)
                .price(price)
                .build();

        em.persist(book);

        return book.getId();
    }

    protected void orderObjectGraph(List<Order> orders) {
        orders.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getDeliveryStatus();
            order.getOrderItems().forEach(orderItem -> {
                orderItem.getItem().getName();
            });
        });
    }

    protected Map<String, Object> getHashMapOrderMemberDelivery(Order order) {
        Map<String, Object> result = new HashMap<>();
        result.put("orderStatus", order.getStatus());
        result.put("memberName", order.getMember().getName());
        result.put("deliveryAddressCity", order.getDelivery().getAddress().getCity());
        return result;
    }
}