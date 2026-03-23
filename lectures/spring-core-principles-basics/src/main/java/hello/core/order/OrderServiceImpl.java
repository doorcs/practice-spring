package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

    // 인터페이스에만 의존. 구체적인 클래스에 대해서는 전혀 모름. DIP를 잘 지키고 있는 상태!
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
        // 이 생성자를 통해서 어떤 구현 객체가 들어올지(주입될지)는 알 수 없다
        // OrderServiceImpl은 이제부터 실행에만 집중하면 된다

        // 배우는 자신의 배역을 수행하는 데 집중해야 한다. 다른 배역에 누가 캐스팅될지 신경쓰는 건 배우의 관심사가 아니다!!
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(member.getId(), itemName, itemPrice, discountPrice);
    }
}
