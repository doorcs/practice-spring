package hello.core.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = context.getBean(StatefulService.class);
        StatefulService statefulService2 = context.getBean(StatefulService.class);

        // A 사용자는 10000원 주문
        statefulService1.order("user A", 10000);
        // B 사용자는 20000원 주문
        statefulService2.order("user B", 20000);

        // A 사용자의 주문 금액을 조회
        int price = statefulService1.getPrice();
        // 10000원이 나와야 하는데, 20000원이 출력된다
        // StatefulService의 `price` 필드는 공유되는 필드인데, 클라이언트에서 이 공유 필드의 값을 변경하기 때문에 문제가 생김
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
