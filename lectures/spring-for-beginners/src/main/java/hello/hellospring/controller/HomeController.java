package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // static 디렉토리에 `index.html`이 있지만, 컨트롤러가 우선이기 때문에 정적 리소스는 무시된다
    public String home() {
        return "home";
    }
}
