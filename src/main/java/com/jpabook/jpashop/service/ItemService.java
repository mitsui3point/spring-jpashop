package com.jpabook.jpashop.service;

import com.jpabook.jpashop.controller.dto.ItemDTO;
import com.jpabook.jpashop.controller.form.BookForm;
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
        Book param = new Book();

        param.setId(dto.getId()); //id 세팅을 임의로하는것은 권한없는 유저가 수정을하거나, id를 임의로 조작변경이 가능하기 때문에 권장하지않는 방법이다..
        param.setName(dto.getName());
        param.setPrice(dto.getPrice());
        param.setStockQuantity(dto.getStockQuantity());
        param.setAuthor(dto.getAuthor());
        param.setIsbn(dto.getIsbn());

        itemRepository.save(param);
        return param.getId();
    }
}
