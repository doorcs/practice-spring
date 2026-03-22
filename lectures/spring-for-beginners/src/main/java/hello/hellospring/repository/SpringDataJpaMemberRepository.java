package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    // 이렇게 `JpaRepository`를 extend하는 인터페이스를 만들어두면
    // `스프링 데이터 JPA`에서 해당 인터페이스의 구현체를 자동으로 만들어서 스프링 빈으로 등록해준다!

    Optional<Member> findByName(String name);
}
