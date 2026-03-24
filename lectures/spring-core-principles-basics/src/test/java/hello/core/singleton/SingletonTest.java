package hello.core.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        MemberService memberService1 = appConfig.memberService();
        MemberService memberService2 = appConfig.memberService();

        // 참조값이 다른 것을 확인해볼 수 있다 (각각 다른 객체가 생성됐다)
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);

        // TPS(Traffic Per Second)가 100이라면..
        // 초당 100개의 객체가 새로 생성되고 동작을 마친 뒤에 소멸되는 것
        // 말도 안되는 메모리 낭비다!!

        // 해결방안: 객체가 `딱 하나`만 생성되고, 공유하도록 설계하면 된다 (싱글톤 패턴 적용)
    }
}
