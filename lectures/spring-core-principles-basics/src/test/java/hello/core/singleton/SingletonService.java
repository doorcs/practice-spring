package hello.core.singleton;

public class SingletonService {

    // 자기 자신을 내부에 `static`으로 하나 가지고 있다
    // 이렇게 하면 클래스 레벨에 올라가기 때문에 딱 하나만 존재하게 된다
    private static final SingletonService instance = new SingletonService();

    // 요 객체 인스턴스가 필요할 경우, `getInstance()` 메서드를 통해서만 조회할 수 있도록 함
    public static SingletonService getInstance() {
        return instance; // 항상 같은 인스턴스를 return
    }

    // 이게 핵심!! 생성자를 외부에서 임의로 호출할 수 없도록 private으로 선언해두기
    private SingletonService() {}

    public void logic() {
        System.out.println("싱글톤 객체의 로직 호출");
    }
}
