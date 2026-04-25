package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
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

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000); // 커넥션 풀에서 커넥션 생성 시간 대기
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection conn1 = dataSource.getConnection(); // DataSource 객체를 생성할 때 설정 정보(URL, USERNAME, PW)를 넣어줬기 때문에
        Connection conn2 = dataSource.getConnection(); // 커넥션 획득시에는 매번 설정 정보들을 넘겨주지 않아도 된다!
        // Connection conn3 = dataSource.getConnection();
        // Connection conn4 = dataSource.getConnection();
        // Connection conn5 = dataSource.getConnection();
        // Connection conn6 = dataSource.getConnection();
        // Connection conn7 = dataSource.getConnection();
        // Connection conn8 = dataSource.getConnection();
        // Connection conn9 = dataSource.getConnection();
        // Connection conn10 = dataSource.getConnection();
        // Connection conn11 = dataSource.getConnection(); // 요청시 커넥션 풀에 남은 커넥션이 없음 -> 일단 기다림 -> Timeout(30초) -> 예외 발생
        log.info("connection = {}, class = {}", conn1, conn1.getClass());
        log.info("connection = {}, class = {}", conn2, conn2.getClass());
    }
}

// 23:32:06.989 [MyPool:connection-adder] DEBUG com.zaxxer.hikari.pool.HikariPool --MyPool - Connection not added, stats (total=10/10, idle=0/10, active=10, waiting=1)
// 23:32:36.710 [MyPool:housekeeper] DEBUG com.zaxxer.hikari.pool.HikariPool --MyPool - Fill pool skipped, pool has sufficient level or currently being filled.
// 23:32:36.961 [Test worker] DEBUG com.zaxxer.hikari.pool.HikariPool --MyPool - Timeout failure stats (total=10/10, idle=0/10, active=10, waiting=0)
//
// MyPool - Connection is not available, request timed out after 29999ms (total=10, active=10, idle=0, waiting=0)
// java.sql.SQLTransientConnectionException: MyPool - Connection is not available, request timed out after 29999ms (total=10, active=10, idle=0, waiting=0)
