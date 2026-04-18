package hello.exception.exhandler;

import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api") // @RestControllerAdvice == @ControllerAdvice + @ResponseBody
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 이게 없으면 상태 코드가 200으로 나간다!!
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST); // 상태 코드를 이런식으로 지정해줄수도 있음
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exhandler(Exception e) { // 실수로 놓친 예외들 or 공통으로 처리하기로 한 예외들을 모두 잡기 위해 최상위 예외인 Exception을 받는다
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}

// 적용 대상이 될 컨트롤러를 지정하는 방법:

// 1. 어노테이션 지정 (특정 어노테이션이 붙은 컨트롤러에만 적용)
// @ControllerAdvice(annotations = RestController.class)

// 2. 패키지 지정 (특정 패키지 안의 컨트롤러에만 적용)
// @ControllerAdvice(basePackages = "org.example.controllers")

// 3. 특정 컨트롤러 클래스 지정 (특정 타입의 컨트롤러에만 적용)
// @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
