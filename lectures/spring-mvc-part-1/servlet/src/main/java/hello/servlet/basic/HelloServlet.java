package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello") // 서블릿 이름과 url 매핑은 중복되면 안 된다!
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("HelloServlet.service");
        System.out.println("req = " + req);
        System.out.println("resp = " + resp);

        String username = req.getParameter("username");
        System.out.println("username = " + username);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8"); // `EUC-KR` 쓰지 마라! `UTF-8` 쓰기
        resp.getWriter().write("hello " + username);
    }
}

// HelloServlet.service
// req = org.apache.catalina.connector.RequestFacade@41c79278
// resp = org.apache.catalina.connector.ResponseFacade@65d54b27
// username = doorcs
