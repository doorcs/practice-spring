package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    // 스프링부트 3.0(스프링 6.0) 이후로는 trailing slash 유무를 구분하도록 변경됨!! `/hello-basic` 과 `/hello-basic/` 은 다른 URL로 인식된다
    @RequestMapping("/hello-basic") // @RequestMapping({"/hello-basic", "hello-go"}) 배열로 여러 URL에 매핑할수도 있다
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    @GetMapping(value = "/mapping-get-v2") // method = RequestMethod.GET
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId = {}", data);
        return "ok";
    }

    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    // 지금처럼 `URL 템플릿의 변수 이름`과 `파라미터의 이름`이 같을 경우 괄호 안에 들어가는 변수 이름 생략 가능!
    // public String mappingPath(@PathVariable("userId") String userId, @PathVariable("orderId") Long orderId) {
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////  이 아래로는 잘 안 쓰는 것들!    //////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json") // 클라이언트에서 서버로 보내는 데이터 타입에 따라 매핑
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html") // 클라이언트에서 요청(요구)하는 데이터 타입에 따라 매핑
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
