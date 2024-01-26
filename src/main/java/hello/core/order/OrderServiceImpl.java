package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@RequiredArgsConstructor // 현재 내 객체에 final 붙은 객체에 파라미터로 받는 생성자를 생성해준다.
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired // 생성자가 하나 일 경우 Autowired 생략가능
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /**
     * @Autowired : 1. 타입으로 매칭.
     *              2. 필드 명 또는 파라미터 명
     */


    /***
     *
     *      스프링 컨테이너 라이프사이클 -> 컨테이너 생성 -> 빈 등록 -> DI 주입 ( Autowired 등록 )
     *
     *      * 생성자 주입
     *     @Autowired
     *     public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
     *         this.memberRepository = memberRepository;
     *         this.discountPolicy = discountPolicy;
     *     }
     *     * 정리
     *     º 생성자 주입 방식을 선택하는 이유는 여러가지 있지만, 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법.
     *     º 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다.
     *       생성자 주입과 수정자 주입을 동시에 사용할 수 없다.
     *     º 항상 생성자 주입을 선택 ! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택하라. 필드 주입은 사용하지 않는게 좋다.
     *
     *      * 수정자 주입
     *     @Autowired
     *     public void setMemberRepository(MemberRepository memberRepository) {
     *         System.out.println("memberRepository = " + memberRepository);
     *         this.memberRepository = memberRepository;
     *     }
     *
     *     @Autowired
     *     public void setDiscountPolicy(DiscountPolicy discountPolicy) {
     *         System.out.println("discountPolicy = " + discountPolicy);
     *         this.discountPolicy = discountPolicy;
     *     }
     *
     *     * 일반 메서드 주입
     *        @Autowired
     *     public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
     *         this.memberRepository = memberRepository;
     *         this.discountPolicy = discountPolicy;
     *     }
     */

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
