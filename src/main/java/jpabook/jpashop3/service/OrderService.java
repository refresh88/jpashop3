package jpabook.jpashop3.service;

import jpabook.jpashop3.domain.Delivery;
import jpabook.jpashop3.domain.Member;
import jpabook.jpashop3.domain.Order;
import jpabook.jpashop3.domain.OrderItem;
import jpabook.jpashop3.domain.item.Item;
import jpabook.jpashop3.repository.ItemRepository;
import jpabook.jpashop3.repository.MemberRepository;
import jpabook.jpashop3.repository.OrderRepository;
import jpabook.jpashop3.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        // 원래는 Delivery, OrderItem도 Repository를 생성해서 save를 한값을 받아서 저장해야하는데,
        // orderRepository.save하나로 되는 이유는 Order 엔티티에 보면 cascade.ALL 옵션 덕분에 가능함.
        // Cascade.ALL 옵션이 있으면, Order가 persist될때 같이 persist가 됨.

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        //주문 취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
