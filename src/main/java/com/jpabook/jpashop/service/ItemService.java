package com.jpabook.jpashop.service;

import com.jpabook.jpashop.controller.dto.ItemDTO;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    @Transactional
    public Long saveItemMerge(Item item) {
        itemRepository.saveMerge(item);
        return item.getId();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public Long saveItem(ItemDTO dto) {
        Book param = Book.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .build();

        param.changeId(dto.getId()); //id 세팅을 임의로하는것은 권한없는 유저가 수정을하거나, id를 임의로 조작변경이 가능하기 때문에 권장하지않는 방법이다..
        param.changeName(dto.getName());
        param.changePrice(dto.getPrice());
        param.changeStockQuantity(dto.getStockQuantity());
        param.changeAuthor(dto.getAuthor());
        param.changeIsbn(dto.getIsbn());

        itemRepository.save(param);
        return param.getId();
    }
}
