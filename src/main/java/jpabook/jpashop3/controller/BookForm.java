package jpabook.jpashop3.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    // 상품 수정을 위한 id
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
