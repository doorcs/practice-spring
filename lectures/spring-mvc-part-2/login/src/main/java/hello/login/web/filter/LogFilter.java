package hello.login.web.filter;

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // log.info("log filter doFilter");

        HttpServletRequest httpRequest =
                (HttpServletRequest) request; // HTTP, 웹 요청을 처리하려면 이렇게 `HttpServletRequest`로 다운캐스팅 해서 써야한다!
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); // 다음 필터가 있다면 다음 필터를, 다음 필터가 없다면 서블릿을 호출함! 이거 꼭 넣어줘야한다!!!
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
} // 이렇게 필터를 만든다고 끝이 아니라, `빈으로 등록` 해줘야한다!
