package hello.core;


import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration // AutoAppConfig에 있는 애너테이션도 Configuration, component란
@ComponentScan(       // @Component 애너테이션이 붙은 클래스를 하나하나 다 찾아서 자동으로 빈에 올려줌
        // default : AutoAppConfig가 있는 위치 ( hello.core - 하위패키지 다 탐색
        // 설정 정보 클래스의 위치를 최상단( 프로젝트 시작 루트 ) 에 두고 basePackages 생략.
//        basePackages = {"hello.core.member", "hello.core.order"}, // member 패키지의 하위 디렉토리, 파일만 찾기.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // 제외할거 선정.
)
public class AutoAppConfig {
    //자바 코드로 일일이 다 @Bean을 넣어야 되는 상황에서 ( 귀찮고, 비용많이듬 ) 자동으로 주입하는 방법.

/*
    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository() { // 자동 빈 등록 vs 자동 빈 등록 에러
        return new MemoryMemberRepository();
    }
*/


}
