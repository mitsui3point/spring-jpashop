package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.enums.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;

        private static Order createOrder(Member member, Delivery delivery, OrderStatus status, OrderItem... orderItems) {
            return Order.builder()
                    .member(member)
                    .delivery(delivery)
                    .status(status)
                    .orderItems(orderItems)
                    .build();
        }

        private static Delivery createDelivery(Address address) {
            return Delivery.builder()
                    .address(address)
                    .build();
        }

        private static OrderItem createOrderItem(Book book, int orderPrice, int count) {
            return OrderItem.builder()
                    .item(book)
                    .orderPrice(orderPrice)
                    .count(count)
                    .build();
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            return Book.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();
        }

        private static Member createMember(Address address, String name) {
            return Member.builder()
                    .name(name)
                    .address(address)
                    .build();
        }

        private static Address createAddress(String city, String street, String zipcode) {
            return Address.builder()
                    .city(city)
                    .street(street)
                    .zipcode(zipcode)
                    .build();
        }

        /**
         * userA
         * JPA1 BOOK
         * JPA2 BOOK
         * userB
         * SPRING1 BOOK
         * SPRING2 BOOK
         */
        public void dbInit1() {

            Address address = createAddress("서울", "1", "1111");

            Member member = createMember(address, "userA");
            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);

            em.persist(member);
            em.persist(book1);
            em.persist(book2);

            OrderItem orderItem1 = createOrderItem(book1, 10000, 1);//cascade order
            OrderItem orderItem2 = createOrderItem(book2, 20000, 2);//cascade order
            Delivery delivery = createDelivery(address);//cascade order

            Order order = createOrder(member, delivery, OrderStatus.ORDER, orderItem1, orderItem2);

            em.persist(order);
        }

        public void dbInit2() {

            Address address = createAddress("진주", "2", "2222");

            Member member = createMember(address, "userB");
            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);

            em.persist(member);
            em.persist(book1);
            em.persist(book2);

            OrderItem orderItem1 = createOrderItem(book1, 20000, 3);//cascade order
            OrderItem orderItem2 = createOrderItem(book2, 40000, 4);//cascade order
            Delivery delivery = createDelivery(address);//cascade order

            Order order = createOrder(member, delivery, OrderStatus.ORDER, orderItem1, orderItem2);

            em.persist(order);
        }
    }
}
