package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 스프링이 뜰 때, 이렇게 `@Configuration` 어노테이션이 붙은 클래스에
public class SpringConfig {

    @Bean // 이 `@Bean` 어노테이션이 있으면
    public MemberService memberService() {
        return new MemberService(memberRepository());
    } // 요 메서드의 로직을 실행해서 스프링 빈으로 등록해준다

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
