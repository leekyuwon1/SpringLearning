package helloo.coree.order;

import helloo.coree.discount.FixDiscountPolicy;
import helloo.coree.member.Member;
import helloo.coree.member.MemberRepository;
import helloo.coree.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository(); // 회원 저장소
    private final FixDiscountPolicy discountPolicy = new FixDiscountPolicy(); // 할인 정책률

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 할인 정책은 할인 정책에 맞게 클래스를 짯다. 단일 책임원칙을 잘 지킨 사례
        Member member = memberRepository.findById(memberId); // 회원 조회를 먼저 하고,
        int discountPrice = discountPolicy.discount(member, itemPrice); // 그 회원조회된 것을 할인정책으로 넣어서 로직 실행 후

        return new Order(memberId, itemName, itemPrice, discountPrice); // 오더 객체 생성
    }
}
