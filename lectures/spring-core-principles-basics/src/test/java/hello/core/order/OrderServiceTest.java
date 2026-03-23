package hello.core.order;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder() {
        // given
        Long memberId = 1L; // long을 써도 되긴 하는데, primitive type을 쓰면 null이 들어갈 수 없다
        Member member = new Member(memberId, "member A", Grade.VIP);
        memberService.join(member);

        // when
        Order order = orderService.createOrder(memberId, "item A", 10000);

        // then
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
