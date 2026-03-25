package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

class PrototypeTest {

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); // 빈을 요청할때마다
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); // init()이 수행된다 (매번 새로운 빈이 만들어지기 때문)

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        // 필요하다면 클라이언트에서 직접 종료 메서드를 호출해줘야 한다
        // prototypeBean1.destroy();
        // prototypeBean2.destroy();
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        void destroy() { // 이 destroy()는 한번도 호출되지 않는다!!!
            System.out.println("PrototypeBean.destroy");
        }
    }
}
