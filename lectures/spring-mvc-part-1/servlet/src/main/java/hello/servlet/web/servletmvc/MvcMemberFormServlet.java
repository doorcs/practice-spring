package hello.servlet.web.servletmvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // `WEB-INF`가 뭔데?
        // 이 경로에 담긴 JSP 파일들은 서버 내부에서만 호출될 수 있다. 외부에서 파일 경로를 통한 직접 접근이 불가능하도록 막는 장치
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        // 다른 서블릿이나 JSP로 이동할 수 있는 기능 (서버 내부에서 다시 호출이 발생한다)
        // 요청 URL이 바뀌는게 아님!
        dispatcher.forward(request, response);
    }
}
