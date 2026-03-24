package hello.core.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        // 셋 다 같은 `MemberRepository` 인스턴스를 참조하고 있다!
        System.out.println("memberService -> memberRepository = " + memberService);
        System.out.println("orderService -> memberRepository = " + orderService);
        System.out.println("memberRepository = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 설정 파일로 넘긴 `AppConfig`도 스프링 빈으로 등록된다!
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean);
        // 출력 결과: `bean = hello.core.AppConfig$$SpringCGLIB$$0@96a75da`
        // 순수한 자바 클래스라면 `bean = hello.core.AppConfig` 이렇게 출력되어야 하는데,

        // 왜 이렇게 되나?
        // `AppConfig` 클래스에 `@Configuration` 어노테이션이 붙어있기 때문에,
        // 내가 작성한 `AppConfig` 클래스가 바로 스프링 빈에 등록되는게 아니라
        // 스프링이 `CGLIB`이라는 바이트코드 조작 라이브러리를 사용해서 `AppConfig` 클래스를 상속받은 클래스를 만들고!
        // 그 상속받은 클래스인 `AppConfig@CGLIB` 클래스를 스프링 빈으로 등록하는 것이기 때문

        // 이런 방식을 통해 싱글톤이 보장되도록 해주는 것

        // 그래서 설정 클래스에 `@Configuration` 어노테이션이 없으면 CGLIB 기술을 활용할 수 없고,
        // 출력을 찍어보면 `bean = hello.core.AppConfig` 처럼 나온다
        // 내가 작성한 `AppConfig` 클래스가 그대로 스프링 빈에 등록되는 것

        // 이러면 CGLIB 기술을 활용하지 못하니 싱글톤이 보장되지 않는다!!!

        // 크게 고민할 게 없음. 스프링 설정 정보에는 항상 `@Configuration` 어노테이션을 붙이면 된다
    }
}
