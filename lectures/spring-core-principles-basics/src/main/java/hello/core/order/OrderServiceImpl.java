package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // 인터페이스에만 의존. 구체적인 클래스에 대해서는 전혀 모름. DIP를 잘 지키고 있는 상태!
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(
            MemberRepository memberRepository,
            // `@Qualifier("mainDiscountPolicy")` 처럼 사용하면 `문자열에서 오타`가 나는 경우를 잡을 수 없다 (main`Discout`Policy 처럼)
            // 이때 `@Qualifier`를 감싸는 커스텀 어노테이션을 만들어서 사용하면 이런 문제를 해결할 수 있다!
            @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(member.getId(), itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
