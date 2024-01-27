package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 *  AutoWried가 여러 빈 매칭이 될 때 @Primary 가 등록되면 최우선 순위가 된다.
 *  Qualifier 가 mainDiscountPolicy 못 찾을 시 스프링 빈에 mainDiscountPolicy 이름을 추가로 찾는다.
 *              하지만 Qualifier는 Qualifier를 찾는 용도로만 사용하길 바란다.
 *
 *  * @Primary 와 @Qualifier 을 사용하는 예시
 *  MainDatabase 와 SubDatabase가 있을 때 가정해보자, MainDatabase 에서 Connnection을 할때도 Qualifier 을 사용하고
 *  SubDatabase에서 Connection을 할때도 Qualifier 을 사용하기가 귀찮다.
 *  그렇기때문에 MainDatabase에선 @Primary 를 사용해줌으로써
 *
**/

//@Primary
//@Qualifier("mainDiscountPolicy") // 문자이기때문에 컴파일 단계에서 잡을 수가 없다.
@Component
@MainDiscountPolicy
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
