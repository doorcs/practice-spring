package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME) // 리플렉션을 활용하려면 런타임까지 어노테이션 정보가 남아있어야 한다. 보통 RUNTIME만 쓰게 될 것!
public @interface Login {}
