package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
//@Qualifier("mainDiscountPolicy")
// Qualifier 가 mainDiscountPolicy 못 찾을 시 스프링 빈에 mainDiscountPolicy 이름을 추가로 찾는다.
// 하지만 Qualifier는 Qualifier를 찾는 용도로만 사용하길 바란다.
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }

    }
}
