package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {

        // save
        Member member = new Member("memberV3", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        log.info("member == findMember = {}", member == findMember); // false
        log.info("member equals findMember = {}", member.equals(findMember)); // true
        // 참조값이 다른데도 equals를 하면 true가 나오는 이유? `Lombok` `@Data`가 `equals()`와 `hashCode()`를 자동으로 만들어주기 때문!!
        assertThat(findMember).isEqualTo(member);
    }
}
