package hello.jdbc.exception.basic;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class UncheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e); // 예외를 로그로 남길 때, 마지막 파라미터로 예외 객체를 넘기면 `해당 예외의 stack trace`가 함께 남는다!
        }
    }

    static class Controller {

        Service service = new Service();

        // 런타임 예외를 사용하고 있기 때문에, 해당 예외에 대한 의존관계가 생기지 않는다!!
        // public void request() throws RuntimeSQLException, RuntimeConnectException {
        public void request() {
            service.logic();
        }
    }

    static class Service {

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        // 런타임 예외를 사용하고 있기 때문에, 해당 예외에 대한 의존관계가 생기지 않는다!!
        // public void logic() throws RuntimeSQLException, RuntimeConnectException
        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {

        // public void call() throws RuntimeConnectException {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {

        // public void call() throws RuntimeSQLException {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) { // 체크 예외를 잡아서
                throw new RuntimeSQLException(e); // 런타임 예외로 바꿔서 던진다!!

                // 체크 예외를 런타임 예외로 변환할 땐 `기존 예외를 반드시 포함`시켜야 한다!!
                // 아래처럼 쓰면 `기존 예외의 stack trace`가 날아가서 `예외의 원인`을 파악할 수 없음:
                // throw new RuntimeSQLException();
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {

        public RuntimeSQLException() {}

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
