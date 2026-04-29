package hello.jdbc.exception.basic;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UncheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request()).isInstanceOf(RuntimeException.class);
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
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
