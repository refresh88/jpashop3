package jpabook.jpashop3.controller;

import jpabook.jpashop3.domain.Member;
import jpabook.jpashop3.domain.Order;
import jpabook.jpashop3.domain.item.Item;
import jpabook.jpashop3.repository.OrderRepository;
import jpabook.jpashop3.repository.OrderSearch;
import jpabook.jpashop3.service.ItemService;
import jpabook.jpashop3.service.MemberService;
import jpabook.jpashop3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderRepository orderRepository;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        // 단순히 service에서 위임만 하는거라면 걍 repository 불러도 됨.(조회인 경우)
        // orderRepository.findAllByString(orderSearch);
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
