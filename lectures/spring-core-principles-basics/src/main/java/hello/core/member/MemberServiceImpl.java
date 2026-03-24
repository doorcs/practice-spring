package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {

    // 이렇게 하면 DIP를 지키는 것!
    // 구체화(구현 클래스)에 대한 정보 없이, 추상화(인터페이스)에만 의존하고 있다
    private final MemberRepository memberRepository;

    @Autowired // ac.getBean(MemberRepository.class) 같은 역할을 한다. 의존관계를 자동으로 주입해줌
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository; // 이런걸 생성자 주입 방식이라고 부른다
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
