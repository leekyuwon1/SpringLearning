package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor // 생성자에 autowired가 자동으로 들어가는 애너테이션
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    //이렇게 되면 MyLogger를 주입받는게 아닌, MyLogger를 찾을 수 있는 즉, DL 을 할 수 있는 빈이 주입이 된다.

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString(); // 고객이 어떤 URL 을 선택했는지 알 수 있다.

//        MyLogger myLogger = myLoggerProvider.getObject(); //new
        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }
}
