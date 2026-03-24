package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary // `@Autowired`에서 빈을 찾을 때 여러 빈이 매칭될 경우, `@Primary`가 붙은 빈이 우선순위를 가진다
@Component
@Qualifier("mainDiscountPolicy") // `@Primary`를 사용하면서 `@Qualifier`를 같이 사용할 수도 있다
public class RateDiscountPolicy implements DiscountPolicy {

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
