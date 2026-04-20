package hello.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // Http 요청 파라미터(쿼리 스트링)은 모두 문자로 처리되기 때문에,
        Integer intValue = Integer.valueOf(data); // 자바에서 다른 타입으로 사용하고 싶다면 이런식으로 변환해줘야 한다
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) { // 데이터를 바로 Integer로 받아서 쓸 수 있는 이유? 스프링이 타입 변환을 해주기 때문
        System.out.println("data = " + data);
        return "ok";
    }
}
