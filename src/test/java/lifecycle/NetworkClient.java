package lifecycle;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect : " + url);
    }

    // 연결이 된 상태에서 call 을 부를수 있다고 가정.
    public void call(String message) {
        System.out.println("call : " + url + " Message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }


    @PostConstruct
    public void init() { // Spring이 DI 하고난 뒤에 콜백을 해준다
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }


    @PreDestroy
    public void close() { // Bean이 종료될 때 호출된다.
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
