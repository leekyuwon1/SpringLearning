package helloo.coree;

import helloo.coree.member.Grade;
import helloo.coree.member.Member;
import helloo.coree.member.MemberService;
import helloo.coree.member.MemberServiceImpl;
import helloo.coree.order.Order;
import helloo.coree.order.OrderService;
import helloo.coree.order.OrderServiceImpl;

public class OrderApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        memberService.join(new Member(memberId, "memberA", Grade.VIP));
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
        System.out.println("order = " + order.calculatePrice());
    }
}
