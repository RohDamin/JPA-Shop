package jpabook.jpashop.controller;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jpabook.jpashop.domain.item.Item;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final EntityManager em;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(BookForm form) {

        Book book = new Book();

        // set 사용하는 것보다 생성 메서드를 만드는 게 더 좋은 설계
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping(value = "/items")
    public String list (Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {

        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();

        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /**
     * 상품 수정 (권장)
     */
    // 컨트롤러에서 어설프게 엔티티를 생성하지 말 것!
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";

        // 컨트롤러에서 엔티티 생성 -> 좋지 않은 코드
        // Book book = new Book();
        // book.setId(form.getId());
        // book.setName(form.getName());
        // book.setPrice(form.getPrice());
        // book.setStockQuantity(form.getStockQuantity());
        // book.setAuthor(form.getAuthor());
        // book.setIsbn(form.getIsbn());
        // itemService.saveItem(book);
        // return "redirect:/items";
    }

    /**
     * 상품 수정
     */

    // 준영속 엔티티: 영속성 컨텍스트가 더이상 관리하지 않는 엔티티
    // 위 코드에서는 Book 객체 -> new로 생성했기 때문에 JPA에서 이를 감지하지 못함

    // 준영속 엔티티를 수정하는 2가지 방법
    // 1. 변경 감지 기능 사용 -> 실무에서 사용!
    @Transactional
    void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회
        findItem.setPrice(itemParam.getPrice()); //데이터를 수정
    }

    // 2. 병합(merge)사용 -> 가능한 쓰지 말것!
    // (1)변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만,
    // (2) 병합을 사용하면 모든 속성이 변경됨
    // 병합시 값이 없으면 null 로 업데이트 할 위험도 있음
    // 병합은 모든 필드를 교체함
    // @Transactional
    // void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    //     Item mergetItem = em.merge(itemParam);
    // }


}

