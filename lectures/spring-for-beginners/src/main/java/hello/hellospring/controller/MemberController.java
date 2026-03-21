package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired // 생성자에 `@Autowired` 어노테이션이 있으면, 필요한 의존성을 스프링 컨테이너에서 찾아서 넣어준다
    public  MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
