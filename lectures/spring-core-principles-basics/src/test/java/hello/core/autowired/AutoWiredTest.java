package hello.core.autowired;

import hello.core.member.Member;
import java.util.Optional;
// import org.springframework.lang.Nullable;
// 스프링 프레임워크 7.0부터는 `jspecify.annotations.Nullable`이 표준!
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class AutoWiredTest {
    
    @Test
    void AutoWiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    // `Member`는 스프링 빈이 아니기 때문에, 아래 세 가지 메서드 모두 자동으로 주입해줄 의존관계가 없는 상황
    static class TestBean {

        // 자동으로 주입해줄 의존관계가 존재하지 않을 경우 메서드 호출 자체가 안 된다
        @Autowired(required = false)
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1 = " + member);
        }

        // 자동으로 주입해줄 의존관계가 존재하지 않을 경우 `null`이 주입된다
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 = " + member);
        }

        // 자동으로 주입해줄 의존관계가 존재하지 않을 경우 `Optional.empty`가 주입된다
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }
    }
}
