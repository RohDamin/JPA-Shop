package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int StockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories;

    //==비즈니스 로직==//
    // 데이터를 가지고 있는 쪽에 비즈니스 로직이 있는 게 관리하기 좋음
    // 바깥에서 setter로 변경X, 비지니스 메서드로 변경

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.StockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.StockQuantity -= quantity;
        if (restStock<0) { // 재고가 0보다 작으면 예외 발생
            throw new NotEnoughStockException("need more stock");
        }
        this.StockQuantity = restStock;
    }
}
