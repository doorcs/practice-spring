package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "spring!!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-spring")
    @ResponseBody
    public String helloSpring(@RequestParam("name") String name) {
        return "hello " + name; // StringConverter (StringHttpMessageConverter)
    }

    @GetMapping("hello-api")
    @ResponseBody // 객체를 return하면서 `@ResponseBody`를 붙이면 기본적으로 JSON으로 변환됨!
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; // JsonConverter (MappingJackson2HttpMessageConverter)
    }

    static class Hello {

        // `자바 빈 규약`을 따르는 객체! (JavaBeans)

        // 1. 파라미터 없는 기본 생성자를 가짐 (다른 생성자가 없으니 여기선 컴파일러가 자동 생성)
        // 2. 필드는 private을 붙여 캡슐화
        // 3. Getter + Setter 메서드를 public으로 제공

        // + `java.io.Serializable`을 구현 (여기선 생략?)

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
