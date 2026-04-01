package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "requestParamServler", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + " = " + request.getParameter(paramName)));

        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println();

        // `username=doorcs&username=hello` 처럼 파라미터 이름은 하나인데 값이 중복된다면?
        // `request.getParameter()`는 하나의 파라미터 이름에 대해 단 하나의 값이 있을때만 사용해야 한다
        // 지금처럼 값이 중복될 경우 `request.getParameterValues()`를 사용해야 한다 (중복으로 보내는 경우가 거의 없기는 하다)

        // 참고로 지금처럼 값이 중복될 때 `request.getParameter()`를 사용하면
        // `request.getParameterValues()`의 `첫번째 값`을 return한다
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name);
        }

        response.getWriter().write("ok");
    }
}
