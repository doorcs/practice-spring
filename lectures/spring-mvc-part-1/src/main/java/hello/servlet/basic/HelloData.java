package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelloData { // JSON 형식을 파싱하기 위한 객체

    private String username;
    private int age;
}
