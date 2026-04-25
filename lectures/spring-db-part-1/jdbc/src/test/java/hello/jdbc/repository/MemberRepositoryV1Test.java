package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // DriverManager 사용
        // DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀 사용 (HikariCP)
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {

        // save
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        log.info("member == findMember = {}", member == findMember); // false
        log.info("member equals findMember = {}", member.equals(findMember)); // true
        // 참조값이 다른데도 equals를 하면 true가 나오는 이유? `Lombok` `@Data`가 `equals()`와 `hashCode()`를 자동으로 만들어주기 때문!!
        assertThat(findMember).isEqualTo(member);

        // update
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

// DriverManager를 쓸 때 (매번 새로 커넥션 생성)

// 23:57:31.127 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = conn1: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class org.h2.jdbc.JdbcConnection
// 23:57:31.145 [Test worker] DEBUG o.s.j.d.DriverManagerDataSource --Creating new JDBC DriverManager Connection to [jdbc:h2:tcp://localhost:1521/test]
// 23:57:31.155 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = conn2: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class org.h2.jdbc.JdbcConnection
// 23:57:31.158 [Test worker] DEBUG o.s.j.d.DriverManagerDataSource --Creating new JDBC DriverManager Connection to [jdbc:h2:tcp://localhost:1521/test]
// 23:57:31.162 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = conn3: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class org.h2.jdbc.JdbcConnection
// 23:57:31.164 [Test worker] DEBUG o.s.j.d.DriverManagerDataSource --Creating new JDBC DriverManager Connection to [jdbc:h2:tcp://localhost:1521/test]
// 23:57:31.171 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = conn4: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class org.h2.jdbc.JdbcConnection

// 커넥션 풀을 쓸 때 (커넥션 재사용!) - 여기서는 병렬 처리가 없기 때문에 전부 `wrapping conn0`
// HikariProxyConnection@xxx 부분(객체 인스턴스 주소)은 달라지는 이유? - 커넥션을 꺼내줄 때, 프록시 객체를 새로 만들고 그 안에 커넥션을 넣어서 반환해주기 때문!!

// 23:58:42.531 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = HikariProxyConnection@1834031967 wrapping conn0: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class com.zaxxer.hikari.pool.HikariProxyConnection
// 23:58:42.535 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = HikariProxyConnection@1092619788 wrapping conn0: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class com.zaxxer.hikari.pool.HikariProxyConnection
// 23:58:42.537 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = HikariProxyConnection@1893899796 wrapping conn0: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class com.zaxxer.hikari.pool.HikariProxyConnection
// 23:58:42.541 [Test worker] INFO  h.jdbc.repository.MemberRepositoryV1 --get conn = HikariProxyConnection@1568949719 wrapping conn0: url=jdbc:h2:tcp://localhost:1521/test user=SA, class = class com.zaxxer.hikari.pool.HikariProxyConnection
