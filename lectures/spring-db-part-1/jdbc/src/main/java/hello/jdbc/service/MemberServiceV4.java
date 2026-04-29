package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional // 요 어노테이션을 트랜잭션 적용이 필요한 메서드에 붙여주기만 하면, 스프링 트랜잭션 AOP에서 트랜잭션, 커밋, 롤백 처리를 알아서 해준다!
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money); // 트랜젝션 관련 코드를 모두 날리고, 비즈니스 로직만 남길 수 있다!!!
    }

    private void bizLogic(String fromId, String toId, int money) {
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
