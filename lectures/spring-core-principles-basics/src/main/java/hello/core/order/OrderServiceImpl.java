package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // 인터페이스에만 의존. 구체적인 클래스에 대해서는 전혀 모름. DIP를 잘 지키고 있는 상태!
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(
            MemberRepository memberRepository,
            // `@Primary`랑 `@Qualifier`중에선 `@Qualifier`가 우선순위를 가진다
            // 따라서 여기서는 `@Primary`인 `rateDiscountPolicy` 빈 대신 `fixDiscountPolicy` 빈이 주입된다
            @Qualifier("fixDiscountPolicy") DiscountPolicy discountPolicy) {
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
