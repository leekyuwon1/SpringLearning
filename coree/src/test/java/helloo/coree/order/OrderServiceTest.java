package helloo.coree.order;

import helloo.coree.member.Grade;
import helloo.coree.member.Member;
import helloo.coree.member.MemberService;
import helloo.coree.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void order(){
        //given
        Long memberId = 1L;

        //when
        memberService.join(new Member(memberId, "memberA", Grade.VIP));
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        //then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
