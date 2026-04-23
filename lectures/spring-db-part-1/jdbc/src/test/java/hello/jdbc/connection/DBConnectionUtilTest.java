package hello.jdbc.connection;

import java.sql.Connection;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}
