package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.controller.dto.ItemDTO;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void saveMerge(Item item) {
        if (item.getId() == null) {
            em.persist(item);
            return;
        }
        em.merge(item);
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i").getResultList();
    }

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
            return;
        }
        updateItem(item);
    }

    private void updateItem(Item item) {
        Item itemEntity = findOne(item.getId());
        itemEntity.setName(item.getName());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setStockQuantity(item.getStockQuantity());
        itemEntity.setCategories(item.getCategories());
    }
}
