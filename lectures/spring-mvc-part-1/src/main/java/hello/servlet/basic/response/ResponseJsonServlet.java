package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content-Type: application/json
        response.setContentType("application/json"); // `application/json`은 스펙 상 `UTF-8` 인코딩을 사용하도록 되어 있다
        response.setCharacterEncoding("UTF-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("doorcs");
        helloData.setAge(27);

        // 위 `helloData` 객체를 `{"username": "doorcs", "age": 27}` 형태의 JSON 문자열로 변환해야 함 (via ObjectMapper)
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);
    }
}

// 별로 중요한 내용은 아니고, 단순 참고:

// `application/json`은 스펙 상 `UTF-8` 인코딩을 사용하도록 되어 있기 때문에,
// 사실 `Content-Type: application/json;charset=utf-8` 처럼 전달하는건 의미 없는 파라미터를 추가로 전달하는 셈이다

// 하지만 `response.getWriter()`를 사용하면 추가 파라미터를 자동으로 추가해버린다
// 이걸 피하려면 `response.getOutputStream()`을 사용해 출력하면 된다
