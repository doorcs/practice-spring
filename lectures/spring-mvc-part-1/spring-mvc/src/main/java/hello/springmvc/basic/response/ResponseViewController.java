package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello!");

        // `@ResponseBody` 어노테이션 없이 String을 return하면 리턴값을 `뷰의 논리적 이름`으로 취급한다
        // 그래서 `뷰 리졸버`가 실행되어 뷰를 찾고, 그 뷰를 렌더링한다

        // `@ResponseBody`가 있으면 `뷰 리졸버가 실행되지 않고`, HTTP 메시지 바디에 문자열을 담아서 응답한다
        return "response/hello";
    }

    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {

        // 1. 리턴 타입이 void
        // 2. `@Controller`를 사용
        // 3. `HttpServletResponse`, `OutputStream` 처럼 HTTP 메시지 바디를 다루는 파라미터가 없음

        // 위 조건을 모두 만족하는 경우, `요청 URL`을 `뷰의 논리적 이름`으로 사용한다

        // 하지만 이렇게 딱 맞는 상황도 잘 없고, 명시성이 너무 떨어지기 때문에 이런 방식은 권장하지 않는다!
        model.addAttribute("data", "hello!!");
    }
}
