package hello.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class CoreApplicationTests {

	@Autowired
	ApplicationContext ac;

	@Test
	void contextLoads() {
		AppConfig bean = ac.getBean(AppConfig.class);
		System.out.println("bean = " + bean);
	}


}
