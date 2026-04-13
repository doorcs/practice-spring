package hello.login.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        // 세션 데이터 출력
        session.getAttributeNames()
                .asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}

// 2026-04-13T15:19:37.797+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : session name=loginMember, value=Member(id=1, loginId=test, name=테스터, password=test!)
// 2026-04-13T15:19:37.798+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : sessionId=C1B1E4037B0FA080511B850F3CC6D157
// 2026-04-13T15:19:37.798+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : maxInactiveInterval=60
// 2026-04-13T15:19:37.798+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : creationTime=Mon Apr 13 15:19:31 KST 2026
// 2026-04-13T15:19:37.798+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : lastAccessedTime=Mon Apr 13 15:19:31 KST 2026
// 2026-04-13T15:19:37.799+09:00  INFO 43207 --- [nio-8080-exec-7] h.l.web.session.SessionInfoController    : isNew=false
