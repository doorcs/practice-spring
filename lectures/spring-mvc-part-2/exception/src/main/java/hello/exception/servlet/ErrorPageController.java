package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); // ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType = {}", request.getDispatcherType());
    }
}

// 2026-04-18T00:07:38.607+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : errorPage 500
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_EXCEPTION: null
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_EXCEPTION_TYPE: null
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_MESSAGE:
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_REQUEST_URI: /error-500
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_SERVLET_NAME: dispatcherServlet
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : ERROR_STATUS_CODE: 500
// 2026-04-18T00:07:38.608+09:00  INFO 45733 --- [exception] [nio-8080-exec-2] h.exception.servlet.ErrorPageController  : dispatchType = ERROR
