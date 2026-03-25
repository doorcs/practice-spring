package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);
        System.out.println("myLogger = " + myLogger.getClass()); // myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0

        myLogger.log("controller test");
        // 사실 클라이언트는 프록시 객체인 `MyLogger$$SpringCGLIB`의 log() 메서드를 호출하는 것
        // 이 프록시 객체는 `request 스코프`의 진짜 MyLogger 객체를 찾고, 진짜 객체의 log() 메서드를 호출해준다

        // 프록시 객체 자체는 싱글톤처럼 등록되어 있으며, `진짜 객체를 찾는 위임 로직`만 수행함
        logDemoService.logic("testId");

        return "OK";
    }
}

// [f6f35570-3888-48e8-9169-e666fb82b7df] request scope bean create: hello.core.common.MyLogger@33c8358
// [f6f35570-3888-48e8-9169-e666fb82b7df] [http://localhost:8080/log-demo] controller test
// [f6f35570-3888-48e8-9169-e666fb82b7df] [http://localhost:8080/log-demo] service id = testId
// [f6f35570-3888-48e8-9169-e666fb82b7df] request scope bean close: hello.core.common.MyLogger@33c8358
// [c56bf295-ab20-4119-a955-e9bfbd3ea6cc] request scope bean create: hello.core.common.MyLogger@6ace32d6
// [c56bf295-ab20-4119-a955-e9bfbd3ea6cc] [http://localhost:8080/log-demo] controller test
// [c56bf295-ab20-4119-a955-e9bfbd3ea6cc] [http://localhost:8080/log-demo] service id = testId
// [c56bf295-ab20-4119-a955-e9bfbd3ea6cc] request scope bean close: hello.core.common.MyLogger@6ace32d6
