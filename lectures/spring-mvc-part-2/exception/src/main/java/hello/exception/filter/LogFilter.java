package hello.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}

// 처음 요청이 들어올 땐 `DispatcherType.REQUEST` ( 요청 아이디: `5b03...` )
// 2026-04-18T00:28:07.234+09:00  INFO 49435 --- [exception] [nio-8080-exec-1] hello.exception.filter.LogFilter         : REQUEST [5b03e6d1-df74-43c3-961e-ebb3b057b082][REQUEST][/error-ex]
// 2026-04-18T00:28:07.246+09:00  INFO 49435 --- [exception] [nio-8080-exec-1] hello.exception.filter.LogFilter         : RESPONSE [5b03e6d1-df74-43c3-961e-ebb3b057b082][REQUEST][/error-ex]

// 에러 페이지로 이동할 때는 `DispatcherType.ERROR` - 별도의 요청이기 때문에 로그에 요청 아이디가 다르게 찍힌다!! ( 요청 아이디: `356f...` )
// 2026-04-18T00:28:07.250+09:00  INFO 49435 --- [exception] [nio-8080-exec-1] hello.exception.filter.LogFilter         : REQUEST [356ffc52-2274-4355-b085-f1aafb40309f][ERROR][/error-page/500]

// 최종적으로 나가는 에러 페이지 응답 ( 요청 아이디: `356f...` )
// 명시적으로 LogFilter를 `DispatcherType.ERROR`에도 등록해뒀기 때문에 이 로그가 찍히는 것이다! 기본값이라면 이 로그는 찍히지 않았을 것
// 2026-04-18T00:28:07.312+09:00  INFO 49435 --- [exception] [nio-8080-exec-1] hello.exception.filter.LogFilter         : RESPONSE [356ffc52-2274-4355-b085-f1aafb40309f][ERROR][/error-page/500]
