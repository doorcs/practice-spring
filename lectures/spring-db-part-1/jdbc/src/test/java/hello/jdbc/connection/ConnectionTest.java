package hello.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection conn1 = DriverManager.getConnection(URL, USERNAME, PASSWORD); // DriverManager를 사용할 경우
        Connection conn2 = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 커넥션을 획득할때마다 설정 정보를 넣어줘야 한다
        log.info("connection = {}, class = {}", conn1, conn1.getClass());
        log.info("connection = {}, class = {}", conn2, conn2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD); // 설정
        useDataSource(dataSource); // 사용 - 사용하는 곳에서는 설정과 관련된 정보를 몰라도 된다!!!
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection(); // DataSource 객체를 생성할 때 설정 정보(URL, USERNAME, PW)를 넣어줬기 때문에
        Connection con2 = dataSource.getConnection(); // 커넥션 획득시에는 매번 설정 정보들을 넘겨주지 않아도 된다!
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }
}
