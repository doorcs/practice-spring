package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
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
