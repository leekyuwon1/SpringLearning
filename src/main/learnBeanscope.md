### _빈 스코프_
* * *

지금까지 우리는 스프링 빈이 스프링 컨테이너의 시작과 함께 생성되어 스프링 컨테이너가 종료될 때까지 유지된다고 학습했다. 
이것은 스프링 빈이 기본적으로 **싱글톤 스코프**로 생성되기 때문이다. 스코프는 번역 그대로 범위를 뜻한다.
<br>

___스프링은 다음과 같은 다양한 스코프를 지원한다.___
* 싱글톤( Default ) : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프.
* 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프.<br>
  >**_참고_** <br>프로토타입을 사용을 언제 해야되는가? <br>
  > javax.inject 패키지에 가보면 DL을 언제 사용하는지에 대한 예시가 Document로 작성되어 있다.<br>
  > 별도의 클래스를 직접 만들게 된다면 의존관계 주입도 직접 다 해주어야 된다. 의존관계 주입이 완료된 객체를 생성해서 받고 싶을 때 프로토타입을 사용하면 된다.

* 웹 관련 스코프
* `request` : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프.
* `session` : 웹 세션이 생성되고 종료될 때까지 유지되는 스코프.
* `application` : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프.

<br>

### _프로토타입 빈_
* * *

**<U>_프로토타입 빈 라이프사이클_</U>** 
> 1번 클라이언트 ( prototypeBean 요청 ) → 스프링 DI 컨테이너 ( 새로운 빈 생성 + DI ) → 1번 클라이언트( prototypeBean 반환 )<br>
> 2번 클라이언트 ( prototypeBean 요청 ) → 스프링 DI 컨테이너 ( 새로운 빈 생성 + DI ) → 2번 클라이언트 ( prototypeBean 반환 )

<br>

**<U>_특징_</U>**
> * 스프링 컨테이너에 요청할 때 마다 **새로 생성**된다.
> * 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
>   * 스프링 DI 컨테이너 생성된 빈을 반환하면 **빈 관리를 하지 못한다**.
> * 종료 메서드가 호출 되지 않는다. ( `@PreDestory` 애너테이션을 사용하지 못한다. )
> * **프로토타입 빈은 클라이언트가 관리 해야한다**. 
> * **종료 메서드에 대한 호출도 클라이언트가 직접 해야한다**.

<br>

**<U>_싱글톤과 프로토타입을 함께 사용시 문제점._</U>**
> * 싱글톤 빈에서 프로토타입 빈을 의존성 주입하는 코드 
> ```java
> @Scope("singleton")
> static class ClientBean {
>   private final PrototypeBean prototypeBean; // 생성 시점에 주입이 되어 있다.
>
>   @Autowired
>   public ClientBean(PrototypeBean prototypeBean) {
>       this.prototypeBean = prototypeBean;
>   }
> }
>  ```
> 이처럼 `ClientBean` 싱글톤 빈이지만, `prototypeBean은` 프로토타입 빈이다. 이렇게 하게 될 시, `prototype` 은 생성 시점에 DI가 주입되어 싱글톤으로 주입이 된다.<br><br>
> 프로토타입의 빈이 의도대로 동작하지 않는 문제점을 발견하였다. <br><br>
> 어떻게하면 싱글톤에서 프로토타입을 유지하며 매번 새로운 빈을 꺼낼 수 있을까?<br><br>
**<U>_핵심 코드 변경( DL( Dependency Lookup ) )_</U>**
> ```java
> @Scope("singleton")
> static class ClientBean{
>   @Autowired
>   ApplicationContext applicationContext;
>   
>   public int logic(){
>       PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
>       prototypeBean.addCount();
>       return prototypeBean.getCount();
>   }
> }
> ```
> 결과는 싱글톤에서 프로토타입 빈을 매번 새로 생성되는것을 확인할 수 있다.<br><br>
> 의존관계를 외부에서 주입( DI )받는 형식이 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 **_Dependency Lookup(DL)_** 의존관계 조회(탐색)이라 한다.<br><br>
> 하지만 이렇게 스프링의 애플리케이션 컨텍스트 전체를 주입받게 된다면 
> * 스프링 컨테이너에 종속적인 코드가 된다.
> * 단위 테스트도 어려워진다.
<br><br>

**<U>_싱글톤 빈과 함께 사용시 Provider_</U>**
* * *

> `ObjectFactory`, `ObjectProvider` 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 제공한다.<br>
> 참고로, 과거에는 `ObjectFactory`를 사용했으나, 편의기능을 추가한 `ObjectProvider` 나오게 되었다.<br>
> 장점은 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트, Mock 코드를 만들기 훨씬 쉽다.<br>
> ```java
> static class ClientBean{
>
>   @Autowired
>   private ObjectProvider<PrototypeBean> prototypeBeanProvider;
>
>   public int logic() {
>   PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
>   prototypeBean.addCount();
>   return prototypeBean.getCount();
>   }
> }
> ```

>**<U>_특징_</U>**
> * **`ObjectFactory`**
>   * 단순하게 `getObject` 하나만 제공한다.
>   * 별도의 라이브러리 필요 없다.
>   * 스프링에 의존
> * **`ObjectProvider`**
>   * `ObjectFactory` 상속하고있다.
>   * `Optional`, `Stream` 처리 등 편의 기능이 많다
>   * 별도의 라이브러리 필요 없다.
>   * 스프링에 의존

**<U>_JSR-330 Provider_</U>**
* * *

JSR 은 자바 스펙 요구서(Java Specification Request, 약자 JSR) 즉, 자바표준을 뜻한다.
이 방법의 단점은 `javax.inject:javax.inject:1` 라이브러리를 gradle에 추가해야 한다.