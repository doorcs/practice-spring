# 스프링 DB 1편 - 데이터 접근 핵심 원리

> 2026.04.29 완강

- JDBC
  - [링크](#jdbc)
- DB 커넥션
  - [링크](#db-커넥션)
- 커넥션 풀
  - [링크](#커넥션-풀)
- 트랜잭션
  - [링크](#트랜잭션)
- 트랜잭션 매니저
  - [링크](#트랜잭션-매니저---platformtransactionmanager)
- 스프링 트랜잭션 AOP
  - [링크](#스프링-트랜잭션-aop---transactional)
- 자바의 예외
  - [링크](#자바의-예외)
- 예외 처리 원칙!
  - [링크](#예외-처리-원칙)

---

## JDBC가 왜 나왔나?

- 애플리케이션 서버와 DB는 주로 TCP/IP를 통해 연결됨 (커넥션)
- 애플리케이션 서버는 DB가 이해할 수 있는 SQL을 전달함
- DB는 전달된 SQL을 수행하고 그 결과를 애플리케이션 서버에 응답함
- 애플리케이션 서버는 DB의 응답값을 활용함

> 여기서 문제는 DB마다 `커넥션을 맺는 방법`, `SQL을 전달하는 방법`, `결과를 응답받는 방법`이 모두 다르다
>
> 여기서 생기는 문제점:
>
> 1. DB 종류를 변경하면 애플리케이션 서버에 있는 DB 사용 코드도 함께 변경해야 한다
> 2. 개발자가 각각의 DB마다 `커넥션 연결`, `SQL 전달`, `결과 응답` 방법을 새로 학습해야 한다

-> 이를 해결하기 위해 `JDBC`라는 자바 표준이 등장!

---

## JDBC?

JDBC: Java DataBase Connectivity  
자바에서 DB에 쉽게 접속할 수 있도록 도와주는 API (Application Programming Interface)

- DB 작업에 꼭 필요한 `3가지 핵심 단계`를 `표준 인터페이스`로 정의해서 제공!
  - `java.sql.Connection`
    - DB와 연결
  - `java.sql.Statement`
    - SQL을 DB에 전달하고 실행
    - 실무에서는 보안과 성능 문제로 `PreparedStatement`를 쓰긴 하지만, 이것도 `Statement`의 하위 인터페이스
  - `java.sql.ResultSet`
    - SQL 처리 결과를 담은 객체 (DB의 응답을 처리하는 방법!)

---

## JDBC와 최신 데이터 접근 기술

JDBC는 1997년에 출시된 `오래된` 기술 - 사용법이 복잡하다  
최근에는 JDBC API를 직접 사용하지 않는 편

- JDBC를 편리하게 사용할 수 있도록 도와주는 기술
  - SQL Mapper
    - JDBC를 편리하게 사용할 수 있도록 도와준다
      - SQL 응답 결과를 객체로 편리하게 변환해준다
      - JDBC의 반복 코드를 제거해준다
      - 하지만.. 개발자가 SQL을 직접 작성해야한다
    - 대표 기술들
      - JdbcTemplate
      - MyBatis
      - ...
  - ORM
    - 데이터베이스 테이블과 객체를 매핑시켜주는 기술
    - ORM 기술이 개발자 대신 동적으로 SQL을 만들고 실행해준다
      - 개발자가 반복적인 SQL을 직접 작성하지 않아도 됨!
    - 각각의 DB마다 SQL문법이 조금씩 다른 문제도 중간에서 해결해준다
    - 대표 기술들
      - JPA
      - Hibernate
      - EclipseLink
      - ...

> 이런 기술들도 내부적으로는 JDBC를 사용하고 있다  
> 그래서 직접 JDBC를 활용하지 않더라도 JDBC의 기본 원리를 알아둬야 한다!!

---

## DB 연결

DB와 연결하려면? JDBC가 제공하는 `DriverManager`의 `.getConnection()` 메서드를 활용하면 된다

이렇게 하면 `DriverManager`에서 라이브러리에 있는 데이터베이스 드라이버를 찾고, 해당 드라이버가 제공하는 DB 커넥션을 return해준다

- DB 연결에 필요한 정보들을 넘겨줘야 함!
  - DB 접속주소
  - DB 유저네임
  - DB 비밀번호
  - ...

```java
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e); // Checked Exception을 Runtime Exception으로 바꿔서 던지기
        }
    }
}

// 이후 사용시:
Connection connection = DBConnectionUtil.getConnection();
```

> JDBC는 `java.sql.Connection`이란 표준 커넥션 인터페이스를 정의
>
> H2 드라이버는 이 인터페이스를 구현한 `org.h2.jdbc.JdbcConnection` 구현체를 제공하는 것!

## DriverManager 커넥션 요청 흐름

JDBC가 제공하는 `DriverManager`는 라이브러리에 등록된 DB 드라이버들을 관리하고, 커넥션을 획득하는 기능을 제공한다

- 애플리케이션 로직에서 커넥션이 필요하다면 `DriverManaver.getConnection()`을 호출한다
  - DriverManager는 라이브러리에 등록된 드라이버 목록을 자동으로 인식한다
  - 인식한 드라이버들에게 DB 연결과 관련된 정보들을 넘겨주고, `해당 드라이버가 커넥션을 획득할 수 있는지` 확인한다
    - URL: `jdbc:h2:tcp://localhost:1521/test`
    - DB 유저네임
    - DB 비밀번호
    - ...
  - 각각의 드라이버는 URL 정보를 체크해서 `해당 드라이버에서 처리 가능한 요청`인지 확인한다
    - URL이 `jdbc:h2`로 시작한다면? `h2 DB`에 접근하기 위한 요청일 것
      - MySQL 드라이버가 해당 요청을 받았다면 `처리할 수 없는 요청`이므로 다음 드라이버에게 순서가 넘어감
      - h2 드라이버가 해당 요청을 받았다면 DB 연결 정보들을 활용해 `커넥션을 획득`한 다음, `획득한 커넥션`을 클라이언트에게 `return`!

> JDBC API CRUD는 코드랑 커밋 코멘트 확인하기
>
> `src/main/java/hello/jdbc/repository/MemberRepositoryV0.java`

---

## DB 커넥션

DB 커넥션을 생성하려면 필요한 과정:

- DB 드라이버를 통해 커넥션 조회
- DB 드라이버를 통해 TCP/IP 커넥션 연결
  - 이 과정에 3-way handshake같은 과정도 포함
- DB 드라이버는 TCP/IP 커넥션이 연결되면 DB 유저네임, DB 비밀번호 및 기타 정보들을 DB에 전달
- DB는 넘겨받은 정보들을 활용해 내부 인증을 완료하고, 내부에 DB 세션을 생성
- DB에서 커넥션 생성이 완료됐다는 응답을 보냄
- DB 드라이버는 커넥션 객체를 생성하고 클라이언트에게 return

> 새롭게 DB 커넥션을 생성하려면 복잡한 연산들이 필요하고 시간도 많이 소요된다
>
> 진짜 문제는, 고객 요청을 처리하기 위해 그때그때 DB 커넥션을 새로 생성한다면  
> SQL을 실행하는 시간 뿐만 아니라 `커넥션을 새로 생성하는데 걸리는 시간`이 추가되어 `전체적인 응답 속도가 느려진다`
>
> -> 사용자 경험이 나빠질 수 있다!!

(물론 DB마다 커넥션 생성에 걸리는 시간은 다르다. 일반적으로 MySQL 계열은 수ms 안에 커넥션을 확보할 수 있지만, 수십ms가 걸리는 DB도 있음!)

## 커넥션 풀

커넥션 생성에 따른 latency 문제를 해결하기 위한 아이디어가 바로 `커넥션 풀`  
커넥션들을 관리하는 풀(수영장 같은 느낌? 인재풀 등록?)

애플리케이션 시작 시점에, 필요한 만큼 DB 커넥션을 미리 확보한 다음 풀에 보관해두는 방식  
커넥션 풀에 들어있는 각각의 커넥션들은 TCP/IP로 DB와 연결되어 있는 상태!  
-> 언제든지 SQL을 DB에 전달할 수 있다

애플리케이션 로직에서 DB 접근이 필요할 경우, 드라이버를 통해 새로운 커넥션을 획득하는 대신  
`커넥션 풀에 있는 커넥션`을 객체 참조로 가져다 쓴다  
(커넥션 풀에 커넥션을 요청하면, 커넥션 풀에 있는 커넥션들 중 하나를 return하는 구조)

풀에서 빌려온 커넥션을 사용해 DB에 접근하고, `사용을 마친 뒤`에는 커넥션을 종료하지 않고 `커넥션 풀에 되돌려준다`

> 적절한 커넥션 풀 크기는 서비스의 특징과 애플리케이션 서버의 스펙, DB서버의 스펙에 따라 달라지기 때문에 `성능 테스트`를 통해서 결정해야 함!
>
> 스프링 부트 2.0부터 기본 커넥션 풀로 사용되고 있는 `HikariCP`의 경우, `커넥션 풀 기본값은 10`이다

---

## DB 커넥션 획득을 추상화 - DataSource

DB 커넥션 획득? 다양한 방법이 있다:

- JDBC의 DriverManager 활용
- HikariCP 같은 커넥션 풀 활용
- ...

만약 애플리케이션에서 `DriverManager`를 통해 DB 커넥션을 획득하다가 `HikariCP` 같은 커넥션 풀을 통해 DB 커넥션을 얻도록 변경한다면  
-> 애플리케이션 코드 중 DB 커넥션을 획득하는 부분도 같이 변경해줘야 한다 (OCP 위반)

이런 문제를 해결하려면? `DB 커넥션 획득 방법을 추상화`하고, 애플리케이션에서는 구현체가 아닌 `추상화에 의존`하도록 코드를 작성 (DIP, OCP 준수!)  
-> DB 커넥션 획득 방법을 변경하게 되더라도 애플리케이션 로직을 변경하지 않아도 된다!!

---

## 트랜잭션

데이터를 그냥 파일에 저장하지 않고 DB를 사용하는 이유? `트랜잭션`

Transaction? 단어 뜻은 `거래`  
즉, 하나의 거래가 안전하게 처리될 수 있도록 보장해주는 것

- 커밋: 작업 묶음의 모든 작업들이 성공해서 데이터베이스에 정상 반영
- 롤백: 작업 묶음 중 실패한 작업이 생겨서 `거래가 시작되기 전으로 되돌리는 것` 즉, 데이터베이스에 반영 X

트랜잭션은 `ACID`를 보장해야 한다

- Atomicity: 원자성
  - 트랜잭션 내에서 실행한 작업들은 `마치 하나의 작업인 것 처럼` 모두 성공하거나 모두 실패해야 한다
  - All or Nothing
- Consistency: 일관성
  - 모든 트랜잭션은 DB의 일관성을 해치지 않아야 한다
  - PK Constraint, FK Constraint, Unique 등 데이터베이스의 무결성 제약 조건을 항상 만족해야 함
- Isolation: 격리성
  - 여러 트랜잭션들이 동시에 실행되더라도 서로의 동작에 영향을 미치지 않아야 한다
    - `동시에 같은 데이터를 수정하지 못하도록!!!!!`
  - 성능과의 트레이드오프가 있기 때문에, 트랜잭션의 격리 수준을 선택할 수 있다
- Durability: 지속성
  - 트랜잭션이 성공적으로 끝나면 그 결과는 디스크에 안전하게 저장되어야 한다
  - 운영 도중 시스템에 문제가 생기더라도 데이터베이스 로그 등을 활용해서 성공한 트랜잭션 내용들을 복원할 수 있어야 함

트랜잭션 격리 수준 4단계:

- Read Uncommitted
- Read Committed
  - 일반적으로 DB의 트랜잭션 격리수준 기본값
- Repeatable Read
- Serializable

---

## DB 세션

DB와의 커넥션이 만들어질 때, DB 서버 내부에서는 `세션`을 만든다  
-> 이 커넥션을 통해 들어오는 모든 요청은 해당 세션을 통해 실행됨

세션은 트랜잭션을 시작하고, 요청받은 작업들을 수행한 다음 커밋 또는 롤백을 통해 트랜잭션을 마친다

---

## 락

동시성 문제를 해결하기 위해 DB에서는 `락`을 사용

- DB의 데이터를 수정하려면 먼저 락을 획득해야 함
  - 락 획득에 실패할 경우 일단 대기
  - Timeout 시간을 넘도록 락을 획득하지 못하면 쿼리 수행 실패
- 락을 획득한 다음 쿼리를 수행하고, commit이나 rollback을 통해 트랜잭션을 종료할 때 락도 같이 반납!
- 일반적으로 조회 명령은 락을 획득하지 않는다
  - 조회에서도 락이 필요할 때가 있다
  - 이럴 땐 `SELECT FOR UPDATE` 구문 활용

---

## 트랜잭션 처리 코드 직접 작성하기

```java
public void accountTransfer(String fromId, String toId, int money) throws SQLException {

    Connection conn = dataSource.getConnection();

    try {
        conn.setAutoCommit(false); // 트랜잭션 시작 (모든 명령이 즉시 커밋되는 자동 커밋 모드를 끄는 것)
        bizLogic(conn, fromId, toId, money);
        conn.commit(); // 예외 없이 여기까지 넘어왔다면 커밋으로 트랜잭션 종료
    } catch (Exception e) {
        conn.rollback(); // 중간에 예외가 발생했다면 롤백으로 트랜잭션 종료
        throw new IllegalStateException(e);
    } finally {
        release(conn); // 자동 커밋 모드 다시 켜두기 + 커넥션 종료 또는 반환 (커넥션 풀 사용시)
    }
}
```

> 트랜잭션의 원리 - `DB 락`

---

## 애플리케이션 구조

다양한 애플리케이션 구조가 있지만, 가장 단순하면서 많이 사용되는 방법은 역할에 따라 `3가지 계층`으로 나누는 것

- 프레젠테이션 계층
  - UI와 관련된 처리 담당
  - 웹 요청과 응답
  - 사용자 요청을 검증
  - 주 사용 기술:
    - 서블릿
    - 스프링 MVC
    - ...
- 서비스 계층
  - `핵심 비즈니스 로직`을 담당
  - 주 사용 기술:
    - `가급적 특정 기술에 의존하지 않도록 순수 자바 코드로 작성`
- 데이터 접근 계층
  - DB 커넥션을 맺고, 데이터베이스에 접근하는 역할을 담당
  - 주 사용 기술:
    - JDBC
    - JPA
    - File
    - Redis
    - Mongo
    - ...

---

## 트랜잭션 처리 코드를 서비스 계층에 직접 작성할때의 문제점

- JDBC 구현 기술이 서비스 계층에 누수되는 문제
  - 계층 간 분리가 제대로 이루어지지 못하고 있다
  - 서비스 계층이 특정 기술(JDBC)에 종속되게 된다
- 트랜잭션 동기화 문제
  - 트랜잭션 처리를 위해서는 DB 접근이 같은 커넥션(DB 세션)에서 이뤄져야 한다
    - 같은 커넥션을 유지하기 위해 `Connection` 객체를 메서드 파라미터로 념겨줘야 한다!
    - 이런 방식 때문에, 같은 기능이라도 `트랜잭션 처리가 필요한 부분`과 `트랜잭션 처리가 필요없는 부분`으로 분리해줘야 한다
- 트랜잭션 적용 코드가 반복되는 문제
  - 핵심 비즈니스 로직과 무관한 `try - catch - finally` 구문이 반복됨
- 예외가 누수되는 문제
  - 데이터 접근 계층에 해당되는 JDBC 구현 기술의 예외가 서비스 계층까지 전파된다
  - 트랜잭션 처리를 위해 서비스 계층에서 `Connection` 객체를 다루고 있기 때문에, 서비스 계층에서 `SQLException`을 처리해줘야 한다
    - `SQLException`은 JDBC 기술의 예외이므로, 추후에 데이터 접근 계층 기술을 변경할 경우 서비스 계층의 코드도 같이 수정해줘야 한다 (OCP 위반)

> 해결 방법? 트랜잭션 관리를 추상화! ( `PlatformTransactionManager` )  
> 서비스 계층이 구현 클래스 대신 추상화에 의존하도록 해주면 된다 (DIP, OCP 준수)

---

## 트랜잭션 매니저 - PlatformTransactionManager

```java
private final PlatformTransactionManager transactionManager; // 트랜잭션 매니저를 통해 트랜잭션을 관리!

public void accountTransfer(String fromId, String toId, int money) throws SQLException {

    // 트랜잭션 시작
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {
        bizLogic(fromId, toId, money);
        transactionManager.commit(status); // 성공시 커밋으로 트랜잭션 종료
    } catch (Exception e) {
        transactionManager.rollback(status); // 실패시 롤백으로 트랜잭션 종료
        throw new IllegalStateException(e);
    }
```

트랜잭션 매니저는 두가지 기능을 한다:

- 트랜잭선 추상화
  - 서비스 계층에서 특정 데이터 접근 계층 기술에 의존하지 않고도 트랜잭션을 관리할 수 있도록 `트랜잭션 처리를 추상화`
  - 아래 세가지 기능을 제공하는 인터페이스:
    - 트랜잭션 시작 - getTransaction()
    - 트랜잭션 커밋 - commit()
    - 트랜잭션 롤백 - rollback()
- 트랜잭션 동기화
  - 트랜잭션 처리를 위해선 DB 접근이 같은 커넥션(DB 세션)에서 이뤄져야 함!
    - 하지만 파라미터로 `Connection` 객체를 전달하는 방식은 코드가 지저분해질 뿐 아니라 여러 단점들이 있음
  - `트랜잭션 매니저`는 내부적으로 `트랜잭션 동기화 매니저`를 사용한다
    - `트랜잭션 동기화 매니저`는 쓰레드 로컬(ThreadLocal) 저장소를 사용해 커넥션을 동기화해준다!

트랜잭션 매니저의 동작 방식:

- 데이터소스를 통해 DB 커넥션을 획득한다
- 트랜잭션을 시작한다
  - 이때 트랜잭션이 시작된 커넥션을 `트랜잭션 동기화 매니저`를 통해 쓰레드 로컬 저장소에 보관한다
- 이후 리포지토리에서는 DB에 접근할 때 `트랜잭션 동기화 매니저`에 보관된 커넥션을 꺼내 사용한다
  - 리포지토리에서 커넥션을 얻어올 때 `Connection conn = DataSourceUtils.getConnection(dataSource)` 처럼 얻어오기 때문
  - 구체적인 동작 방식:
    - 현재 쓰레드에 트랜잭션이 있는지 확인
    - `트랜잭션 동기화 매니저`에 저장된 커넥션이 있는지 확인
    - 있다면 저장된 커넥션을 return
    - 없다면 DataSource를 통해 새로운 커넥션을 획득한 다음 return

---

## 트랜잭션 템플릿 - TransactionTemplate

```java
private final TransactionTemplate txTemplate;
private final MemberRepositoryV3 memberRepository;

public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
    // TransactionTemplate 자체를 빈으로 등록해두지 않고 이렇게 만들어서 사용하는 이유? 더 유연하게 사용할 수 있기 때문!
    this.txTemplate = new TransactionTemplate(transactionManager);
    this.memberRepository = memberRepository;
}

public void accountTransfer(String fromId, String toId, int money) throws SQLException {

    txTemplate.executeWithoutResult((status) -> {
        try {
            // 비즈니스 로직
            bizLogic(fromId, toId, money);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    });
}
```

트랜잭션 템플릿의 기본 동작:

- `언체크 예외`가 발생하면 트랜잭션을 롤백
- `체크 예외`가 발생하면 트랜잭션을 커밋
  - 체크 예외가 발생하면 기본적으로 롤백해주지 않는다!!
  - `SQLException`은 체크 예외이기 때문에, 여기서는 예외가 발생했을 때 롤백시켜주기 위해 언체크 예외인 `IllegalStateException`으로 바꿔주는 것

> 템플릿 콜백 패턴! - 전략 패턴(Strategy Pattern)의 확장 같은 느낌?
>
> 콜백 함수를 통해 런타임에 실행할 코드를 전달해주는 패턴!  
> 트랜잭션 템플릿이 제공하는 콜백 안에서 비즈니스 로직을 실행하면 -> 트랜잭션 템플릿이 알아서 트랜잭션 시작, 커밋, 롤백을 해줌

---

## 스프링 트랜잭션 AOP - `@Transactional`

`트랜잭션` 기능은 `매우 중요한 기능`이고, 전세계 누구나 사용하는 기능이다!

- 스프링에서는 트랜잭션을 손쉽게 처리할 수 있도록 도와주는 `@Transactional` 어노테이션을 지원!
- 개발자는 트랜잭션 처리가 필요한 곳에 `@Transactional` 어노테이션을 붙여주기만 하면 된다
  - 그럼 스프링 트랜잭션 AOP가 `트랜잭션 프록시`를 자동으로 적용해준다!!

스프링에서 AOP를 적용하려면 아래 빈들이 스프링 컨테이너에 등록되어 있어야 한다 (스프링 부트를 사용하면 자동으로 등록됨)

- 어드바이저: `BeanFactoryTransactionAttributeSourceAdvisor`
- 포인트컷: `TransactionAttributeSourcePointcut`
- 어드바이스: `TransactionInterceptor`

> 자세한 내용은 스프링 핵심 원리 고급편에서 다룰 것!

---

## 스프링부트의 자동 리소스 등록

> 스프링부트에서는 다양한 객체들을 스프링 빈으로 자동 등록해준다!

### DataSource

스프링부트는 데이터소스( `DataSource` )를 스프링 빈으로 `자동으로 등록`해준다!

- 이때 자동으로 등록되는 데이터소스의 스프링 빈 이름은 `dataSource`
- 개발자가 직접 데이터소스 빈을 등록해줄 경우, 스프링부트에서는 `dataSource` 빈을 자동으로 등록하지 않는다

데이터소스를 자동으로 등록? 어떻게?  
`application.properties`나 `application.yml` 파일의 속성 정보들을 활용해서 데이터소스를 생성하고, 이를 스프링 빈으로 등록해준다!

- application.properties

```properties
spring.datasource.url=jdbc:h2:tcp://localhost:1521/test
spring.datasource.username=sa
spring.datasource.password=
```

- application.yml

```yml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost:1521/test
    username: sa
    password:
```

### PlatformTransactionManager

스프링부트는 트랜잭션 매니저 ( `PlatformTransactionManager` )도 스프링 빈으로 자동 등록해준다!!

- 이때 자동으로 등록되는 트랜잭션 매니저의 스프링 빈 이름은 `transactionManager`
- 데이터소스와 마찬가지로, 개발자가 직접 트랜잭션 매니저 빈을 등록해줄 경우 스프링부트에서는 `transactionManager` 빈을 자동으로 등록하지 않는다
- 이때 어떤 트랜잭션 매니저를 선택할지는 등록되어 있는 라이브러리( `build.gradle` 등 )에 따라 결정된다
  - JDBC 라이브러리가 등록되어 있다면 `DataSourceTransactionManager` 타입의 빈이 등록됨
  - JPA 라이브러리가 등록되어 있다면 `JpaTransactionManager` 타입의 빈이 등록됨

> 트랜잭션 매니저의 클래스 이름이 `PlatformTransactionManager`인데는 역사적인 이유가 있다
>
> `TransactionManager` 가 EJB에서 이미 사용중인 이름이었다고 함 ㅋㅋ

---

## 자바의 예외

자바 예외의 상속 구조

- `Object` - 모든 자바 객체의 부모
  - `Throwable` - 최상위 예외
    - `Error` - 메모리 부족, 시스템 오류 등 `애플리케이션에서 복구 불가능한 시스템 예외`
    - `Exception` - 체크 예외, `애플리케이션에서 사용할 수 있는 실질적인 최상위 예외`
      - `RuntimeException` - 언체크 예외!

`Error`

- 애플리케이션 개발자는 `Error`를 잡으려고 하면 안된다! 시스템에 문제가 생긴 상황이므로 프로그램이 터지게 둬야 함

`Exception`

- 체크 예외!
- `Exception`과 그 하위 예외들은 컴파일러가 체크하는 `체크 예외` ( `RuntimeException` 제외! )
- 애플리케이션에서 사용할 수 있는 `실질적인 최상위 예외`

`RuntimeException`

- 언체크 예외!!
- 얘도 `Exception`의 하위 예외긴 한데..
- `RuntimeException`과 그 하위 예외들 ( `NullPointerException`, `IllegalArgumentException` 등 )은 컴파일러가 체크하지 않는 `언체크 예외`
- 런타임 예외라고도 부른다

> 예외는 `폭탄 돌리기`다!

1. 예외는 `try-catch`로 잡아서 처리하거나 밖으로 던져야 한다
2. 예외를 잡거나 던질 땐 지정한 예외 뿐만 아니라 `그 예외의 하위 예외들까지 모두 같이 처리`된다!!
   - `Exception`을 catch하면 그 하위 모든 예외들을 잡을 수 있다
   - `Exception`을 throw하면 그 하위 모든 예외들을 던질 수 있다

- 체크 예외
  - `try-catch`로 잡아서 처리하지 않았다면, 반드시 `throws Xxx` 형태로 예외를 밖으로 던져야 한다
    - `throws` 구문이 없으면 `컴파일 오류가 발생!` 체크 예외는 컴파일러가 체크해주는 예외
  - 장점: 개발자가 실수로 예외 처리를 누락하는 일이 없도록 컴파일러가 막아주는 `안전장치`
  - 단점: 개발자가 모든 체크 예외를 반드시 처리(잡거나 던지기)해야 하기 때문에 번거롭다 + `상위 계층이 하위 계층의 예외에 의존하게 된다`

- 언체크 예외 (런타임 예외)
  - 잡아서 처리하거나 밖으로 던져야한다는 점은 체크 예외와 같다
  - 하지만! 예외를 잡아서 처리하지 않는 경우에도 `throws 구문을 생략`할 수 있다
    - 이럴 경우 자동으로 예외를 밖으로 던지게 된다
  - 장점: 신경쓰고 싶지 않은 언체크 예외를 무시할 수 있다 (신경쓰고 싶지 않은 예외의 의존관계를 몰라도 된다!)
  - 단점: 개발자가 `실수`로 예외 처리를 누락할 수 있다

---

## 예외 처리 원칙!

1. `기본적으로 런타임 예외`를 사용
   - 대신 런타임 예외는 놓치기 쉽기 때문에, 문서화를 잘 해둬야 한다!
   - 생략할 수도 있지만, 중요한 예외는 throws 구문을 남기기도 한다
2. `체크 예외`는 `비즈니스 로직상 의도적으로 예외를 던지는 경우`에만 사용
   - 예시?
     - 계좌이체 실패 예외
     - 결제 시 포인트 부족 예외
     - 로그인 ID, PW 불일치 예외
   - 이런 경우들도 반드시 체크 예외로 만들어야 하는 것은 아니지만, `개발자가 실수로 예외를 놓치면 안되는 경우` 체크 예외 사용을 고려

체크 예외를 사용할때의 문제점?

- 사실 `대부분의 예외는 복구가 불가능`하다!
  - 일부 복구가 가능한 예외도 있긴 하지만 많지 않다
  - `SQLException`의 경우를 보면, 데이터베이스에 뭔가 문제가 있어서 발생하는 예외다
    - SQL 문법에 문제가 있을 수도 있고
    - 데이터베이스 자체에 뭔가 문제가 발생했을 수도 있다
    - `애플리케이션, 즉 서비스나 컨트롤러 계층에서 이런 문제를 해결할 수는 없다!!`
  - 즉, 예외를 밖으로 던지는 것 외에는 어차피 할 수 있는게 없다
- 체크 예외로 인해 생기는 `의존관계 문제`
  - Repository에서 SQLException, ConnectException을 던진다면?
    - Service, Controller에서는 해당 예외를 처리할 수 없다 (애플리케이션 수준에서 복구 불가능한 예외)
    - 잡아서 처리할 수 없으니 밖으로 던져야 함
    - Service와 Controller에 `throws SQLException, ConnectException` 같은 throws 구문이 붙게 된다!!!
      - 서비스와 컨트롤러가 `SQLException`과 `ConnectException`에 의존하게 되는 것
      - 추후에 데이터 접근 계층 기술을 변경하게 된다면, SQLException에 의존하고 있던 `모든 서비스와 컨트롤러 코드를 같이 변경`해줘야 한다..! (OCP 위반)

> 예외가 발생할 경우 `오류 로그`를 남기고, `개발자가 해당 오류를 빠르게 인지`할 수 있도록 하는 방식으로 대응해야 한다
>
> 이런건 체크 예외를 사용한다고 해결되는 부분이 아님!

---

## 체크 예외를 런타임 예외로 바꿔주기

런타임 예외를 활용하면 `throws 구문을 생략`할 수 있다  
-> 상위 계층(Service)에서 하위 계층(Repository)의 예외로 인한 의존관계가 생기지 않는다! (OCP 준수)

```java
static class Service {

    Repository repository = new Repository();

    // 하위 계층에서 런타임 예외를 사용하고 있기 때문에 의존관계가 생기지 않는다!!
    // public void logic() throws RuntimeSQLException
    public void logic() {
        repository.call();
    }
}

static class Repository {

    // public void call() throws RuntimeSQLException {
    public void call() {
        try {
            runSQL();
        } catch (SQLException e) { // 체크 예외를 잡아서
            throw new RuntimeSQLException(e); // 런타임 예외로 바꿔서 던진다!!

            // 아래처럼 쓰면 `기존 예외의 stack trace`가 날아가서 `예외의 원인`을 파악할 수 없음:
            // throw new RuntimeSQLException();
        }
    }

    public void runSQL() throws SQLException {
        throw new SQLException("ex");
    }
}

static class RuntimeSQLException extends RuntimeException {

    public RuntimeSQLException() {}

    public RuntimeSQLException(Throwable cause) {
        super(cause);
    }
}
```

> 이렇게 체크 예외를 런타임 예외로 변환할 땐  
> 반드시 `기존 예외를 포함시켜주기`!!

---

## 스프링의 데이터 접근 계층 예외

자바 초기에는 사람들이 체크 예외를 쓰는게 좋다고 생각했다  
그래서 SQLException, ConnectException 등 자바에서 제공하는 기능들은 체크 예외를 던지는 경우가 많다

하지만 `어차피 애플리케이션에서 처리할 수 없는 예외`가 대부분이기도 하고, `상위 계층에서 하위 계층 예외에 의존`하게 되는 문제점이 있다  
-> 요즘 나오는 라이브러리들은 기본적으로 런타임 예외를 사용함

스프링에서도 체크 예외의 문제점들을 해결하기 위해 다양한 런타임 예외들을 제공하고 있다

스프링에서 제공하는 데이터 접근 계층 예외들의 상속 구조

- `RuntimeException`
  - `DataAccessException`
    - `NonTransientDataAccessException`
      - `BadSqlGrammarException`
      - `DataIntegrityViolationException`
        - `DuplicateKeyException`
        - ...
      - ...
    - `TransientDataAccessException`
      - `QueryTimeoutException`
      - `OptimisticLockingFailureException`
      - `PessimisticLockingFailureException`
      - ...

`DataAccessException`

- 스프링이 제공하는 데이터 접근 계층의 최상위 예외
- 런타임 예외를 상속받음. 즉, `스프링이 제공하는 데이터 접근 계층 예외들은 모두 런타임 예외`

`TransientDataAccessException`

- 일시적일 수 있는 데이터 접근 예외
- 다시 시도하면 성공할 가능성이 있다

`NonTransientDataAccessException`

- 일시적이지 않은(영구적인) 데이터 접근 예외
- 몇 번을 다시 시도해도 계속 실패할 것

---

## 스프링에서 제공하는 예외 변환기

예외 코드 정보를 활용하면 SQLException 같은 체크 예외를 적절한 런타임 예외로 변환해줄 수 있다:

```java
try {
    // ...
} catch (SQLException e) { // SQLException은 체크 예외!
    if (e.getErrorCode() == 23505) { // h2 DB의 키 중복 에러코드
    throw new BadSqlGrammarException(e); // 스프링에서 제공하는 데이터 접근 계층 예외 (런타임 예외)
    }
}
```

근데 매번 예외코드를 다 적어줄 수 없음. 번거롭기도 하고 유지보수가 말이 안 됨  
-> 스프링에서 제공해주는 예외 변환기를 활용할 수 있다!

```java
private final SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator();

try {
    // ...
} catch (SQLException e) {
    throw exTranslator.translate("task", sql, e);
    // 파라미터 설명:
    // String task - readable text describing the task being attempted
    // @Nullable String sql - the SQL query or update that caused the problem (if known)
    // SQLException e - the offending SQLException
}
```

---

## JdbcTemplate

템플릿 콜백 패턴을 활용하는 JdbcTemplate를 쓰면  
DB 커넥션 획득, SQL 실행, 자원 반납 등의 작업들을 수행하기 위해 반복되는 코드를 제거할 수 있다!

- Before

```java
private final DataSource dataSource;
private final SQLExceptionTranslator exTranslator;

public MemberRepositoryV4_2(DataSource dataSource) {
    this.dataSource = dataSource;
    this.exTranslator = new SQLErrorCodeSQLExceptionTranslator();
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
```

- After

```java
private final JdbcTemplate template;

public MemberRepositoryV5(DataSource dataSource) {
    this.template = new JdbcTemplate(dataSource);
}

@Override
public void delete(String memberId) {
    String sql = "DELETE FROM member WHERE member_id=?";
    template.update(sql, memberId);
}
```
