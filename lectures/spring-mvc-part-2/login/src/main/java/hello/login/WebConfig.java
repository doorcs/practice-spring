package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // @Bean
    // public FilterRegistrationBean logFilter() {
    //     FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
    //     filterRegistrationBean.setFilter(new LogFilter());
    //     filterRegistrationBean.setOrder(1);
    //     filterRegistrationBean.addUrlPatterns("/*");

    //     return filterRegistrationBean;
    // }

    @Bean // 구현한 필터를 빈으로 등록
    public FilterRegistrationBean<LogFilter> logFilter() { // 템플릿 파라미터로 필터 타입 넣어주기!
        FilterRegistrationBean<LogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 이 필터가 필터 체인에서 가장 먼저 실행되도록 지정
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter() {
        FilterRegistrationBean<LoginCheckFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
