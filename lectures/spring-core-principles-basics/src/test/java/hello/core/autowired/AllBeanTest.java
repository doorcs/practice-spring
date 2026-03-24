package hello.core.autowired;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class); // 아래에 있는 static class
        Member member = new Member(1L, "user A", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        // DiscountService는 Map을 사용해서 DiscountPolicy 타입의 모든 빈을 주입받은 상태 (fixDiscountPolicy, rateDiscountPolicy)
        // discountService.discount() 메서드는 `discountCode` 파라미터에 따라 다양한 DiscountPolicy 중 하나를 선택해서 사용!
    }

    static class DiscountService {

        private final Map<String, DiscountPolicy> policyMap;
        // key: `스프링 빈 이름`, value: `DiscountPolicy 타입으로 조회한 스프링 빈`
        private final List<DiscountPolicy> policies;
        // `DiscountPolicy 타입으로 조회한 모든 스프링 빈`이 들어감

        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);

            System.out.println("discountCode = " + discountCode);
            System.out.println("discountPolicy = " + discountPolicy);

            return discountPolicy.discount(member, price);
        }
    }
}

// new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

// 1. `new AnnotationConfigApplicationContext()` 로 스프링 컨테이너를 생성
// 2. 파라미터로 받은 `AutoAppConfig.class`, `DiscountService.class`를 스프링 빈으로 등록
