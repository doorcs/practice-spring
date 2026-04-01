package hello.springmvc.basic;

import lombok.Data;

@Data
public class HelloData {

    private String username;
    private int age;
}

// 롬복의 `@Data`: 아래 롬복 어노테이션들의 기능을 한번에 처리해주는 어노테이션
// - @Getter
// - @Setter
// - @ToString
// - @EqualsAndHashCode
// - @RequiredArgsConstructor
