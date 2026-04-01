package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 일반적인 `@Controller`에서 String을 return하면 `뷰 이름`으로 해석된다. 그래서 뷰를 찾게 되고 뷰를 렌더링한다.
// `@RestController`는 `HTTP 메시지 바디`에 `데이터를 그대로 넣어서 반환`한다. return 값을 `뷰 이름으로 해석하지 않는다`
@RestController
public class LogTestController {

    // private final Logger log = LoggerFactory.getLogger(LogTestController.class);
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        // log.trace("trace log = " + name);
        // 로그 남길때 이런식으로 문자열 concat 쓰면 혼난다!!
        // 로그 레벨이 trace가 아닐 경우 해당 로그는 출력되지 않는데, 문자열을 합치는 연산은 로그 레벨과 상관없이 항상 실행되기 때문에 서버에 불필요한 부하를 준다!!!

        log.trace("trace log = {}", name); // 가장 상세하고 사소한 로그, 코드의 실행 흐름을 추적하기 위해 사용하는 로그
        log.debug("debug log = {}", name); // 개발 서버에서 필요한 디버깅 용도의 로그
        log.info(" info log = {}", name); // 비즈니스 정보 등 운영 시스템에서도 봐야 하는 정보성 로그 (기본값)
        log.warn(" warn log = {}", name); // 경고성 로그, 애플리케이션 자체는 동작하지만 잠재적인 문제나 위험이 있음을 알려주는 로그
        log.error("error log = {}", name); // 에러 로그,

        return "ok";
    }
}

// 2026-04-01T15:14:36.579+09:00  INFO 52694 --- [springmvc] [nio-8080-exec-1] h.springmvc.basic.LogTestController      :  info log = Spring
// 2026-04-01T15:14:36.580+09:00  WARN 52694 --- [springmvc] [nio-8080-exec-1] h.springmvc.basic.LogTestController      :  warn log = Spring
// 2026-04-01T15:14:36.580+09:00 ERROR 52694 --- [springmvc] [nio-8080-exec-1] h.springmvc.basic.LogTestController      : error log = Spring
