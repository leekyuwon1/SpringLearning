## _빈 스코프_
* * *

지금까지 우리는 스프링 빈이 스프링 컨테이너의 시작과 함께 생성되어 스프링 컨테이너가 종료될 때까지 유지된다고 학습했다. 
이것은 스프링 빈이 기본적으로 **싱글톤 스코프**로 생성되기 때문이다. 스코프는 번역 그대로 범위를 뜻한다.
<br>

___스프링은 다음과 같은 다양한 스코프를 지원한다.___
* 싱글톤( Default ) : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프.
* 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프.<br>
* 웹 관련 스코프
* `request` : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프.
* `session` : 웹 세션이 생성되고 종료될 때까지 유지되는 스코프.
* `application` : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프.

<br>

### _프로토타입 빈_
* * *

**<U>_프로토타입 빈 라이프사이클_</U>**<br><br> 
1번 클라이언트 ( prototypeBean 요청 ) → 스프링 DI 컨테이너 ( 새로운 빈 생성 + DI ) → 1번 클라이언트( prototypeBean 반환 )<br><br>
2번 클라이언트 ( prototypeBean 요청 ) → 스프링 DI 컨테이너 ( 새로운 빈 생성 + DI ) → 2번 클라이언트 ( prototypeBean 반환 )

<br>

**<U>_특징_</U>**
* 스프링 컨테이너에 요청할 때 마다 **새로 생성**된다.
* 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
  * 스프링 DI 컨테이너 생성된 빈을 반환하면 **빈 관리를 하지 못한다**.
* 종료 메서드가 호출 되지 않는다. ( `@PreDestory` 애너테이션을 사용하지 못한다. )
* **프로토타입 빈은 클라이언트가 관리 해야한다**. 
* **종료 메서드에 대한 호출도 클라이언트가 직접 해야한다**.

<br>

### **<U>_싱글톤과 프로토타입을 함께 사용시 문제점._</U>**
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

<br>

### **<U>_싱글톤 빈과 함께 사용시 Provider_</U>**
* * *

> `ObjectFactory`, `ObjectProvider` 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 제공한다.<br>
> 참고로, 과거에는 `ObjectFactory`를 사용했으나, 편의기능을 추가한 `ObjectProvider` 나오게 되었다.<br><br>
> **_변경 코드_**
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

**<U>_장점_</U>**
* 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트가 가능하다.
* Mock 코드를 만들기 훨씬 쉽다.

**<U>_특징_</U>**
* **`ObjectFactory`**
  * 단순하게 `getObject` 하나만 제공한다.
  * 별도의 라이브러리 필요 없다.
  * 스프링에 의존
* **`ObjectProvider`**
  * `ObjectFactory` 상속하고있다.
  * `Optional`, `Stream` 처리 등 편의 기능이 많다
  * 별도의 라이브러리 필요 없다.
  * 스프링에 의존

<br>

### **<U>_JSR-330 Provider_</U>**
* * *

JSR 은 자바 스펙 요구서(Java Specification Request, 약자 JSR) 즉, 자바표준을 뜻한다.
이 방법은 위의 스프링 의존성이 마음에 들지 않을 때 자바 표준을 이용하는 방법이다. 
사용 방법은 `'jakarta.inject:jakarta.inject-api:2.0.1'` 라이브러리를 gradle에 추가해야 한다.

> **_변경 코드_**
> ```java
> import jakarta.inject.Provider;
> 
> static class ClientBean{
>  
>   @Autowired
>   private Provider<PrototypeBean> prototypeBeanProvider;
>
>   public int logic() {
>   PrototypeBean prototypeBean = prototypeBeanProvider.get();
>   prototypeBean.addCount();
>   return prototypeBean.getCount();
>   }
> }
> ```
> * 의도한대로 `provider.get()` 을 통해 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.
> * `provider` 의 `get()` 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환.(DL)
> * 자바 표준이며, 기능이 단순하므로 단위테스트하거나 mock 코드를 만들기 훨씬 쉬워진다.
>   * 스프링이 아닌 다른 컨테이너에서도 사용이 가능하다.
> * 별도의 라이브러리 필요.

<br>

### **<U>_프로토타입을 사용을 언제 해야되는가?_</U>**
***
javax.inject 패키지에 가보면 DL을 언제 사용하는지에 대한 예시가 Document로 작성되어 있다.<br>
일단, 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다. 
* 매번 사용할 때 마다 의존관계 주입이 완료된 **새로운 객체**가 필요할때.
* 인스턴스를 지연 혹은 선택적으로 찾아야 하는 경우
* 순환 종속성을 깨기 위해서
* 스코프에 포함된 인스턴스로부터 더 작은 범위의 인스턴스를 찾아 추상화 하기 위해서 사용한다.

> **자바 표준과 스프링에서 제공하는 기능 중 무엇을 사용해야 되는가?**<br>
> 대부분 스프링이 더 다양하고 편리한 기능을 제공해주기 때문에, 특별히 다른 컨테이너를 사용할 일이 없다면, 스프링이 제공하는 기능을 사용하자.
> 하지만 스프링이 아닌 다른 컨테이너에서도 사용할 수 있어야 된다면, 자바 표준을 이용하자.

<br>

### **<U>_웹 스코프_</U>**
***

**특징**
* 웹 환경에서만 동작한다. 
* 프로토타입과 다르게 스프링이 해당 스코프의 종료 시점까지 관리하며, 종료메서드가 호출된다.

**종류**
* **request** : HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성및 관리된다.
* **session** : HTTP Session 과 동일한 생명주기를 가지는 스코프
* **application** : 서블릿 컨텍스트(`ServletContext`)와 동일한 생명주기를 가지는 스코프
* **websocket** : 웹 소켓과 동일한 생명주기를 가지는 스코프

네 종류 모두 범위는 다르지만 동작 방식은 비슷하므로 request 스코프를 예제로 설명한다.

<br>

### **<U>_request 스코프 예제_</U>**
***
 
#### build.gradle에 웹 환경 추가
```groovy
// web 라이브러리 추가
implementation 'org.springframework.boot:spring-boot-starter-web'
```
* 해당 라이브러리는 스프링 부트가 내장 톰켓 서버를 활용하여 웹 서버와 스프링을 함께 실행시킨다.
* 웹 라이브러리가 없으면 지금까지 학습한 `AnnotationConfigApplicationContext` 을 기반으로 애플리케이션이 구동한다.
* 웹 라이브러리가 추가되면 웹과 관련된 추가 설정과 환경들이 필요하므로 `AnnotationConfigServletWebServerApplicationContext` 를 기반으로 애플리케이션을 구동한다.
* 만약 기본 포트인 8080 포트를 다른곳에서 사용중이라면 오류가 나므로, 포트 변경은 다음 설정을 추가.
  * `main/resources/application.properties`
  ```properties
    server.port=9090
  ```
#### 코드 작성

<details>
<summary>MyLoger</summary>

```java
@Component
@Scope(value = "request") // 값이 하나일 땐 @Scope("request")라고 해도 상관없다.
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        String uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope been create: " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope been close: " + this);
    }
}
```
</details>

<details>
<summary>LogDemoController</summary>

```java
@Controller
@RequiredArgsConstructor // 생성자에 autowired가 자동으로 들어가는 애너테이션
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURI().toString(); // 고객이 어떤 URL 을 선택했는지 알 수 있다.
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
```
</details>


<details>
<summary>LogDemoService</summary>

```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}

```
</details>

#### 기대와 결과
기대 값은 아래와 같이 로그 확인 결과이다. 
```
[d06b992f...] rEquest scope bean create
[d06b992f...] [http://localhost:8080/log-demo] controller test
[d06b992f...] [http://localhost:8080/log-demo] service id = testId
[d06b992f...] request scope bean close 
```
하지만, 결과 값은 아래와 같이 오류가 뜬다.
```
Caused by: org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:373)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1441)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1348)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:911)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:789)
	... 33 common frames omitted
```
해당 프로젝트가 구동될때 스프링 빈들이 컴포넌트 스캔이 되며 등록 및 의존관계 주입이 되는데, 여기서 웹스코프인 MyLogger 빈의 경우 
HTTP request 요청이 올때 생성되는 빈이기 때문에 스프링 구동단계에서는 아직 생성을 할 수 없다. 그렇기에 해당 에러가 발생하는 것이다.

정리하면, 스프링 구동 시 스프링 빈을 등록하게 되는데 `MyLogger`의 빈은 `request` 빈 스코프라 구동이 될 때 생기는 빈이 아닌 웹 요청이 있을 때 생기는 빈이라 오류가 발생. 

그러면 `request` 빈을 사용하면서 스프링을 실행하는 방법은 어떤것들이 있을까?

<br>

### 스코프와 Provider
***

앞서 학습한 Provider 를 이용하는 방법이다.

<br>

<details>

<summary> 1. ObjectProvider 적용한 코드 </summary>
<br>

* LogDemoController
```java
@Controller
@RequiredArgsConstructor 
public class LogDemoController {

  private final LogDemoService logDemoService;
  private final ObjectProvider<MyLogger> myLoggerProvider;

  @RequestMapping("log-demo")
  @ResponseBody
  public String logDemo(HttpServletRequest request) {
      String requestURL = request.getRequestURL().toString(); 

      MyLogger myLogger = myLoggerProvider.getObject(); 

      myLogger.setRequestURL(requestURL);

      myLogger.log("controller test");
      logDemoService.logic("testId");
      return "OK";
  }
}
```
<br>

* LogDemoService
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

  private final ObjectProvider<MyLogger> myLoggerProvider;

  public void logic(String id) {
      MyLogger myLogger = myLoggerProvider.getObject();
      myLogger.log("service id = " + id);
  }
}
```

* ObjectProvider 를 이용하여 `getObject()` 를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다.
* `getObject()` 를 호출하는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리된다.
* `getObject()` 를 컨트롤러, 서비스에서 각각 호출을 하는데도 동일한 HTTP 요청일 경우 같은 스프링 빈이 반환된다.
</details>

<details>
<summary>2. 프록시 적용한 코드</summary>

프록시란? <br>원본 객체를 감싸고, 다른 객체에 대한 접근을 제어한다. 그리고 호출을 중간에서 가로채는 객체를 뜻한다.

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    
}
```

* `proxyMode = ScopedProxyMode.TARGET_CLASS)` 를 추가.
  * 적용 대상이 클래스일 경우 `TARGET_CLASS`
  * 적용 대상이 인터페이스일 경우 `INTERFACES`
* 이 클래스는 가짜 프록시 클래스를 만들고 이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.
* 해당 빈( `request` )을 사용하게 될 땐 프록시 빈에서 실제 빈을 가져와 사용할 수 있도록 해준다.
</details>
sdsfasfsa

