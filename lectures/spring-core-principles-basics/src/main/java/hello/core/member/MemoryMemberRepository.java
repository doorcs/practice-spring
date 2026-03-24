package hello.core.member;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // 실무에서는 ConcurrentHashMap을 써야 함

    @Override
    public void save(Member member) {
        store.put(member.getId(), member); // 원래는 예외 처리가 들어가야 하는데, 단순한 예제니까 생략
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
