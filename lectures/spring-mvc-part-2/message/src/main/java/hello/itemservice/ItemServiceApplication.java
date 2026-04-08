package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

// 스프링 부트를 쓰면 이런 빈을 자동으로 만들어준다! 직접 등록 안해도 됨
//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasenames("messages", "errors");
//		messageSource.setDefaultEncoding("UTF-8");
//		return messageSource;
//	}

}
