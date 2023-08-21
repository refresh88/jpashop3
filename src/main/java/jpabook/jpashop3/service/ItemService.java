package jpabook.jpashop3.service;

import jpabook.jpashop3.domain.item.Book;
import jpabook.jpashop3.domain.item.Item;
import jpabook.jpashop3.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity,
                           String author, String isbn) {
        // 트랜잭션 안에서 엔티티를 조회하고 -> 영속성 컨텍스트의 감시 대상이 된다.
        // 데이터를 수정한다 -> 변경 감지 사용!
        Book findItem = (Book) itemRepository.findOne(itemId);

        findItem.change(name, price, stockQuantity);
        // 이런식으로 의미있는 메서드를 만들어서 설정해야지 밑에 set메서드 쓰는건 좋지 않음.
        // 이렇게 해야 변경 지점이 엔티티로 모여짐.
//        findItem.setName(name);
//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);
        findItem.setAuthor(author);
        findItem.setIsbn(isbn);
        // findItem은 영속성 컨텍스트가 관리하고 있기 떄문에
        // 굳이 save를 하지 않아도 변경감지가 일어나서 update를 한다.
//        itemRepository.save(findItem);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
