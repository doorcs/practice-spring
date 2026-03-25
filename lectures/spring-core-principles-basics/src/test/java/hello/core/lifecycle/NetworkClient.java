package hello.core.lifecycle;

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

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    @PostConstruct // 스프링에 종속적인 기술이 아니라, 자바 표준이다 (JSR-250)
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy // 최신 스프링에서 가장 권장되는 방식
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
