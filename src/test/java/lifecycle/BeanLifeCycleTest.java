package lifecycle;

import hello.core.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    // 데이터베이스 커넥션 풀, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 종료하는 작업을 진행하려면
   // 객체의 초기화와 조욜 작업이 필요하다.
    @Test
    public void lifeCycleTest() {
        // ApplicationContext 에서는 close()메서드를 제공되지 않는다.
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {
        // Bean LifeCycle -> 객체 생성 -> 의존관계 주입 ( set DI방식, Field DI 방식 ) ( Construct DI 방식은 예외 )
        // Spring Bean Event LifeCycle =
        // Spring Container Create -> Spring Bean Create -> DI( set, Field Injection ) -> Using -> disconnect CallBack -> Spring End
        //                        Construct DI 경우 이 단계에서 일어남
        // 초기화 콜백 : 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
        // 소멸전 콜백 : 빈이 소멸되기 직전에 호출
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }

    }
}
