package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        // TransactionTemplate 자체를 빈으로 등록해두지 않고 이렇게 만들어서 사용하는 이유? 더 유연하게 사용할 수 있기 때문!
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) {

        txTemplate.executeWithoutResult((status) -> {
            try {
                // 비즈니스 로직
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
                // 트랜잭션 템플릿의 기본 동작:
                // `언체크 예외`가 발생하면 트랜잭션을 롤백
                // `체크 예외`가 발생하면 트랜잭션을 커밋!! 체크 예외가 발생하면 기본적으로 롤백해주지 않는다

                // `SQLException`은 체크 예외이기 때문에, 예외가 발생했을 때 롤백시켜주기 위해 언체크 예외인 `IllegalStateException`으로 바꿔주는 것
            }
        });
        // 템플릿 콜백 패턴! - 전략 패턴(Strategy Pattern)의 확장 같은 느낌?
        // 콜백 함수를 통해 런타임에 실행할 코드를 전달해주는 패턴

        // 트랜잭션 템플릿이 제공하는 콜백 안에서 비즈니스 로직을 실행하면 -> 트랜잭션 템플릿이 알아서 트랜잭션 시작, 커밋, 롤백을 해줌
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
