package hello.hellospring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // 진짜 스프링을 띄워서 테스트
@Transactional // 테스트에 `@Transactional`이 있으면, 테스트 시작 전에 트랜잭션을 걸고 테스트가 끝난 뒤에 롤백해준다
class MemberServiceIntegerationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("hello");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));// `Cmd + Opt + V` 단축키로 Introduce Variable

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        // assertThrows()는 리턴값이 있어서, 필요하다면 예외 메시지 내용까지도 검증해볼 수 있다!
    }
}
