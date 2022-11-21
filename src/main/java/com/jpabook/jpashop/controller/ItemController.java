package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";
    }

    @GetMapping("/items")
    public String itemList(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable(value = "itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm bookForm = new BookForm();
        bookForm.setId(item.getId());
        bookForm.setName(item.getName());
        bookForm.setPrice(item.getPrice());
        bookForm.setStockQuantity(item.getStockQuantity());
        bookForm.setIsbn(item.getIsbn());
        bookForm.setAuthor(item.getAuthor());

        model.addAttribute("bookForm", bookForm);

        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable(value = "itemId") Long itemId,
                             @ModelAttribute("bookForm") @Valid BookForm bookForm) {
        Book item = new Book();

        item.setId(bookForm.getId()); //id 세팅을 임의로하는것은 권한없는 유저가 수정을하거나, id를 임의로 조작변경이 가능하기 때문에 권장하지않는 방법이다..
        item.setName(bookForm.getName());
        item.setPrice(bookForm.getPrice());
        item.setStockQuantity(bookForm.getStockQuantity());
        item.setAuthor(bookForm.getAuthor());
        item.setIsbn(bookForm.getIsbn());

        itemService.saveItem(item);

        return "redirect:/items";
    }
}
