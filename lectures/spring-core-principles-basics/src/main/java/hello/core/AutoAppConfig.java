package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
// 실무에서 `@ComponentScan`을 쓰는 경우 excludeFilters 옵션을 안 주는데, 예제에서 만들었던 Config 클래스들을 살려두기 위한 것
public class AutoAppConfig {}

// `@ComponentScan`은 `@Component`가 붙은 모든 클래스를 스프링 빈으로 등록해준다
// 이때 스프링 빈 이름은 기본적으로 클래스명을 사용하되, 맨 앞글자만 소문자로 바꿔서 등록한다

// 예를 들어 `MemberServiceImpl` 클래스에 `@Component`가 붙어있다면, 스프링 빈 이름은 `memberServiceImpl`이 됨

// `@Component("memberService2") 처럼 등록될 스프링 빈 이름을 명시적으로 지정해줄 수도 있다

// + `@Component` 클래스의 생성자에 `@Autowired` 어노테이션이 있으면, 필요한 의존관계들을 스프링이 자동으로 주입해준다
// 이때 기본 전략은 `타입이 같은 빈`을 찾아서 주입해주는 것!
