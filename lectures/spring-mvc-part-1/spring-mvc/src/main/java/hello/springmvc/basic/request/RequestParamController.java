package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody // 뷰를 조회하지 않고, return 값을 HTTP 메시지 바디에 담아주도록 하는 어노테이션
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username = {}, age = {}", memberName, memberAge);
        return "ok";
    }

    /**
     * HTTP 요청의 파라미터 이름이 변수명과 같으면 `@RequestParam("xxx")` 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * String, int, Integer 등의 단순 타입이면 `@RequestParam` 어노테이션 자체도 생략 가능
     *
     * 하지만 `@RequestParam` 어노테이션 자체를 생략하는건 조금 과하다는 느낌이 있다
     * `HTTP 요청 파라미터에서 값을 읽는다`는걸 명확하게 알려주기 위해 `@RequestParam` 어노테이션은 적어주는 것을 권장!
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * 주의!
     * `/request-param-required?username=` 처럼 호출하면 빈 문자열 취급이라 예외가 발생하지 않는다!!
     *
     * 주의!
     * int형에 null을 입력하는 것은 불가능하기 때문에, `required = false` 옵션을 사용하려면 래퍼 타입인 Integer를 써야 함! (또는 defaultValue 사용)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {

        // int a = null; // 컴파일 오류
        // Integer a = null; // 이건 가능

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * defaultValue는 빈 문자열인 경우에도 적용된다
     * 그래서 `/request-param-default?username=` 처럼 호출하면 username에 기본값인 "guest"가 들어감
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터의 값이 1개인 게 확실하다면 Map을 써도 되지만,
     * 파라미터의 값이 여러 개일 수 있다면 MultiValueMap을 써야 한다! (애매하게 key 하나에 대한 value를 여러 개 쓰는 경우가 거의 없기는 하지만..)
     * `?userIds=id1&userIds=id2`
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
