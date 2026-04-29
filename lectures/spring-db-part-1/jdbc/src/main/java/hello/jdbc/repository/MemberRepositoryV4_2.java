package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.*;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator();
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO member(member_id, money) VALUES(?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // 준비한 쿼리를 실제로 DB에 전달해서 실행
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("save", sql, e);
        } finally {
            // 이런애들도 실행하다가 중간에 예외가 터질 수 있음 -> try-catch로 다 잡아줘야함 ㅋㅋㅋ
            // pstmt.close();
            // conn.close();
            close(conn, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findById", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "UPDATE member SET money=? WHERE member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate(); // executeUpdate()는 쿼리를 수행하고 `이 쿼리의 영향을 받은 row 수`를 return!
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            throw exTranslator.translate("update", sql, e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "DELETE FROM member WHERE member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("delete", sql, e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의!! 트랜잭션 동기화를 사용하려면 `DataSourceUtils`를 사용해야한다
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    private Connection getConnection() throws SQLException {
        // 주의!! 트랜잭션 동기화를 사용하려면 `DataSourceUtils`를 사용해야한다
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get conn = {}, class = {}", conn, conn.getClass());
        return conn;
    }
}
