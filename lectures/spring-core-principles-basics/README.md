## 스프링 핵심 원리 - 기본편

> 2026.03.25 완강

## 스프링이 왜 나왔나?

옛날 옛적엔 자바 진영에 `EJB`라는 표준 기술이 있었다. Enterprise Java Beans  
이게 정파였음. 이 표준을 바탕으로 구현한 구현체들을 판매하는 회사들이 많았다

컨테이너 기술, 분산 시스템 지원, 선언적 트랜잭션 + `Entity Bean`이라는 ORM까지 있었음  
-> 대신 비쌈. 한 대에 수천만원짜리 서버가 필요했다

이게 이론은 참 좋은데, 진짜 복잡하고 어렵고 느렸다  
`EJB`에 의존적으로 개발해야 했고, 코드가 참 지저분해졌다  
특히 `Entity Bean`은 기술적인 완성도가 너무 낮았음. 조인 하면 잘 안되고 그랬음..

Rod Johnson - `EJB`를 대체하기 위한 `스프링`  
Gavin King - `Entity Bean`을 대체하기 위한 `하이버네이트`  
-> 이걸 바탕으로 `JPA`라는 표준 인터페이스를 만듦 (80% 이상은 하이버네이트 구현체 사용중)

로드 존슨이 EJB를 비판하는 책을 씀. (3만줄 이상의 예제 코드 포함..)  
-> 이걸 보고 Juergen Hoeller(유겐 휠러), Yann Caroff(얀 카로프)가 로드 존슨에게 오픈소스 프로젝트를 제안함.  
그 프로젝트가 바로 스프링

전통적인 J2EE(EJB)란 겨울을 넘어 새로운 시작이라는 뜻으로 `Spring`이란 이름을 붙였다고 함. 진짜 `봄`이라는 뜻 ㅋㅋ

2003 - 스프링 프레임워크 1.0 출시 - XML 기반 설정  
2006 - 스프링 프레임워크 2.0 출시 - XML 설정을 도와주는 편의기능 지원  
2009 - 스프링 프레임워크 3.0 출시 - XML 대신 자바 코드로 설정할 수 있게 됨  
2013 - 스프링 프레임워크 4.0 출시 - 자바 8 지원  
2014 - 스프링 부트 1.0 출시 - 설정 요소를 대폭 줄여줌 + 내장 웹서버 지원  
2017 - 스프링 프레임워크 5.0, 스프링 부트 2.0 출시 - 리액티브 프로그래밍 지원 (비동기)

2020.09 기준 스프링 프레임워크 5.2.x, 스프링 부트 2.3.x  
2026.03 기준 스프링 프레임워크 7.0.6, 스프링 부트 4.0.4

예전엔 `스프링 개발은 설정이 절반이다`란 농담이 있었을정도로 설정할 게 많았음. 지금은 스프링 부트가 나와서 굉장히 편해졌다

---

## 스프링?

스프링은 하나의 기술이 아니라 여러 기술들의 묶음이다  
스프링 프레임워크, 스프링 부트, 스프링 데이터, 스프링 세션, 스프링 시큐리티, 스프링 배치, 스프링 클라우드, 스프링 RestDocs, ...  
`스프링 프레임워크`가 핵심 기술이고, `스프링 부트`는 스프링 프레임워크를 편리하게 사용할 수 있도록 지원해주는 기술 (최근에는 모든 실무 프로젝트에서 기본으로 사용)

그래서 스프링부트의 장점이 뭔데?

단독으로 실행할 수 있는 스프링 애플리케이션을 쉽게 생성할 수 있게 해 줌  
예전에는.. 스프링으로 프로젝트를 하려면 빌드를 하고, 별도로 설치해둔 톰캣 서버에 빌드한 스프링 프로젝트를 넣고 이걸 띄우고.. 복잡했다  
-> 스프링부트는 Tomcat 같은 웹 서버를 내장해서 별도의 웹 서버를 설치하지 않아도 됨  
손쉬운 빌드 구성을 위한 `starter` 의존성 제공 (필요한 의존성 묶음들을 편하게 땡겨올 수 있게 도와줌)  
\+ 외부 라이브러리의 버전까지 자동으로 챙겨줌  
이게 진짜 큰데, 예전에는 `스프링 프레임워크 3.1이다 하면 어떤 외부 JSON 라이브러리랑은 잘 안맞는다` 이게 진짜 힘들었다.  
그런데 스프링부트를 쓰면, 스프링 프레임워크 5.1에서는 외부 라이브러리들을 A, B, C를 쓴다 이런걸 챙겨준다.  
유명한 메이저 라이브러리들은 스프링이랑 궁합이 잘 맞는지 테스트해서 버전까지 다 지정해뒀다. 이런걸 고민 안 해도 되는게 엄청나게 크다!  
메트릭, 상태 확인, 외부 구성 등 프로덕션에 필요한 기능들을 제공함. 운영 환경에서는 모니터링이 굉장히 중요한데, 스프링부트가 기본적으로 제공해준다

스프링이라는 단어는 문맥에 따라 다르게 사용된다

- 스프링 DI 컨테이너 기술 (스프링 빈을 관리하는 핵심 기술!)
- 스프링 프레임워크
- 스프링 부트, 스프링 프레임워크 등을 모두 포함한 스프링 생태계

그래서 스프링을 왜 만들었나? 스프링의 핵심 컨셉이 뭔데?

스프링은 자바 언어 기반의 프레임워크.  
자바의 가장 큰 특징? `객체 지향 프로그래밍 언어`  
스프링은 객체 지향 언어가 가진 강력한 특징들을 살려내는 프레임워크  
즉, `좋은 객체 지향 애플리케이션`을 개발할 수 있게 도와주는 프레임워크!

---

## 좋은 객체 지향 프로그래밍?

객체지향의 특징:

- 추상화
- 캡슐화
- 상속
- 다형성

객체지향 프로그래밍이 뭔데?

”객체 지향 프로그래밍은 컴퓨터 프로그램을 명령어의 목록으로 보는 시각에서 벗어나 여러개의 독립된 단위, 즉 `객체`들의 모임으로 파악하고자 하는 것이다. 각각의 객체는 메시지를 주고받고, 데이터를 처리할 수 있다 (협력)“  
”객체 지향 프로그래밍은 프로그램을 유연하고 변경이 용이하게 만들기 때문에 대규모 소프트웨어 개발에 많이 사용된다“

핵심 키워드: `객체들의 모임`, `메시지를 주고받고 데이터를 처리`, `유연하고 변경에 용이`

유연하고 변경에 용이??

- 레고 블럭 조립하듯이
- 키보드나 마우스를 갈아 끼우듯이
- 컴퓨터 부품을 갈아 끼우듯이
- 컴포넌트를 쉽고 유연하게 변경하면서 개발할 수 있는 방법

객체지향의 핵심은 `다형성` (Polymorphism)

`역할`과 `구현` (역할이 인터페이스, 구현이 실제 구현체라고 보면 됨)

자동차라는 `인터페이스`에 대한 사용법만 알면, 실제 구현체(K3, 아반떼, 테슬라 모델3, ...)가 어떤 것이든 운전할 수 있다!  
자동차의 내부적인것까지는 몰라도 상관 없다. 심지어는 차가 달라져도 운전을 할 수 있다  
결국 `운전자`의 편의를 위한 것. 더 구체적으로는, 클라이언트(고객)에 영향을 주지 않으면서도 새로운 기능을 제공할 수 있다

`역할과 구현으로 구분하면 세상이 단순해지고, 유연해지며, 변경도 편리해진다`

- 클라이언트는 대상의 역할(인터페이스)만 알면 된다
- 클라이언트는 구현 대상의 내부 구조를 몰라도 된다
- 클라이언트는 구현 대상의 내부 구조가 변경되어도 영향을 받지 않는다
- 클라이언트는 구현 대상 자체를 변경해도 영향을 받지 않는다

프로그래밍 언어에서도 이런 개념을 차용한 것

- 자바 언어의 다형성을 활용!
    - 역할 = 인터페이스
    - 구현 = 인터페이스를 구현한 클래스, 구현 객체
- 객체를 설계할 때 `역할`과 `구현`을 명확히 분리
- 객체 설계시 역할(인터페이이스)을 먼저 부여하고, 그 역할을 수행하는 구현 객체 만들기

`객체의 협력`이라는 관계부터 생각해야 한다

- 혼자 있는 객체는 없다
    - 클라이언트: 요청
    - 서버: 응답
- 수많은 객체 클라이언트와 객체 서버는 서로 `협력 관계`를 가진다

다형성의 본질?

- 인터페이스를 구현한 객체 인스턴스를 `실행 시점`에 `유연하게 변경`할 수 있다
- 다형성의 본질을 이해하려면 `협력`이라는 객체 사이의 관계에서 시작해야 함

`클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다!`

정리하자면..

역할과 구현을 분리함으로써:

- 다형성을 통해, 실세계의 역할과 구현이라는 편리한 컨셉을 객체 세상으로 가져올 수 있다
- 유연하고 변경에 용이
- 확장 가능한 설계
- 클라이언트에 영향을 주지 않는 변경이 가능
- `인터페이스를 안정적으로 잘 설계하는 것이 중요`

한계점:

- 역할(인터페이스) 자체가 변하면.. 클라이언트, 서버 모두에 큰 변경이 발생한다
    - 자동차를 비행기로 변경해야 한다면?
    - 연극 대본 자체가 변경된다면?
    - USB 인터페이스가 변경된다면?
- 다시 강조, `인터페이스를 안정적으로 잘 설계하는 것이 중요`
    - 인터페이스가 변하지 않을 수 있도록 잘 설계하는 개발자가 잘하는 개발자다

여기까지가 모두 스프링 얘기를 위한 빌드업.

스프링과 객체 지향

- 다형성이 핵심이다!
- 스프링은 다형성을 극대화해서 이용할 수 있게 도와준다
- 스프링에서 얘기하는 제어의 역전(IoC), 의존관계 주입(DI)은 모두 다형성을 활용해서 역할과 구현을 편리하게 다룰 수 있도록 지원하는 기능이다
- 스프링을 쓰면 마치 `레고 블럭을 조립하듯이`, `공연 무대의 배우를 선택하듯이` 구현을 편리하게 변경할 수 있다

---

## SOLID

- SRP: 단일 책임 원칙 (Single Responsibility Principle)
- OCP: 개방-폐쇄 원칙 (Open-Closed Principle)
- LSP: 리스코프 치환 원칙 (Liskov Substitution Principle)
- ISP: 인터페이스 분리 원칙 (Inteface Segregation Principle)
- DIP: 의존관계 역전 원칙 (Dependency Inversion Principle)

### SRP

- 한 클래스는 `하나의 책임`만 가져야 한다
- 하나의 책임이라는 말은 모호하다
    - 클 수도 있고, 작을 수도 있다
    - 문맥과 상황에 따라 다르다
- 중요한 기준은 `변경`이다. `변경이 있을 때 파급효과가 적다`면 단일 책임 원칙을 잘 따른 것

### OCP

- SW 요소는 `확장에는 열려`있으나 `변경에는 닫혀`있어야 한다
- `다형성`을 활용
- 인터페이스를 구현하는 새로운 클래스를 만들어서 새로운 기능을 구현

> 이걸 지키려면, 즉 정말로 `기존 코드의 변경 없이 다형성을 활용`하려면 `객체를 생성하고 객체들간의 관계를 맺어주고 조립`하는 별도의 설정자가 필요하다!!
> OCP 원칙을 지키기 위해 DI, IoC가 필요한 것이다

### LSP

- 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다
- 다형성, 상속 관계에서 `하위 클래스는 인터페이스의 규약을 다 지켜`야 한다는 것!!
    - 결국 이것도 다형성을 지원하기 위한 원칙이다
    - 인터페이스를 구현한 구현체들을 믿고 사용하려면 이 원칙이 반드시 필요
    - 단순히 컴파일 오류가 나지 않는다고 끝이 아니다
        - ex) 자동차라는 인터페이스에서 엑셀은 앞으로 가는 기능이다
        - 이걸 뒤로 가게 구현한다면 LSP 위반이다
        - 느리더라도 앞으로 간다면 LSP를 지키고 있는 것

### ISP

- 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다
- 기능(인터페이스)을 `적당한 크기에 맞게 잘 쪼개`는게 중요하다는 것
    - “자동차” 라는 큰 인터페이스를 “운전”, “정비” 인터페이스로 분리할 수 있다
    - 이러면 “사용자”라는 큰 클라이언트도 마찬가지로 “운전자”, “정비사” 클라이언트로 분리할 수 있다
    - -> 이제 정비랑 관련된 부분이 변해도 “운전자” 클라이언트한테 영향을 주지 않게 된다!!
- 인터페이스가 명확해지고, 대체 가능성이 높아진다
    - 덩어리가 작아야 그 덩어리를 다시 구현하기 쉬워지니까
    - 인터페이스가 너무 크면 그걸 다 구현하기 어렵고, 구현체를 바꾸기도 어려워진다

### DIP

- 프로그래머는 `추상화에 의존해야지, 구체화에 의존하면 안돼` ??
- 쉽게 이야기하자면, 클라이언트 코드가 구현 클래스를 바라보지 말고 인터페이스만 바라보게 하라는 뜻
    - 멤버 서비스가 `MemberRepository`라는 인터페이스만 바라보고, `MemoryMemberRepository`, `JdbcMemberRepository` 같은 구현체에 대해서는 몰라야 한다
    - 운전자는 “자동차”라는 인터페이스에 대해 알아야지, “K3" 같은 구현체에 대해 디테일하게 알 필요가 없다
- `역할`에 의존하게 해야 한다는 말과 결국 같은 말이다. 유연하게 변경 가능한 설계!

정리하자면

- 객체지향의 핵심은 다형성
- 하지만 다형성만으로는 쉽게 부품을 갈아끼우듯이 개발할 수 없다
- 다형성만으로는 OCP, DIP를 지킬 수 없다

---

## 다시 스프링으로. 스프링 얘기를 하는데 왜 자꾸 객체지향 얘기가 나오나?

- `스프링`은 `다형성 + OCP, DIP`를 가능하게 지원!
    - DI(Dependency Injection): 의존관계, 의존성 주입
    - DI 컨테이너 제공
- 클라이언트 코드의 변경 없이 기능을 확장할 수 있음
- 쉽게 부품을 교체하듯이 개발할 수 있음

진짜 최종 정리

- 모든 설계에 `역할`과 `구현`을 분리하자
    - 자동차, 공연 예시를 떠올려보기
- 애플리케이션 설계도 공연을 설계하듯 배역만 만들어두고, 배우는 언제든지 유연하게 변경할 수 있도록 만드는게 좋은 객체지향 설게다
- 이상적으로는 모든 설계에 인터페이스를 부여하자
    - 하부 구현요소 선택을 최대한 미룰 수 있다는 장점이 있다
    - 아직 할인 정책같은게 구체적으로 정해지지 않았을 경우, 간단하게 인터페이스를 만들어두고 개발부터 진행 -> 기획에서 정리가 되면 기능을 확장할 수 있다

\+ 실무적인 고민

- 하지만 인터페이스를 도입하면 ”추상화“라는 비용이 발생한다. 성능에 대한 얘기가 아님
    - MemberRepository 라는 인터페이스를 보면, 어떤 구현 클래스가 사용됐는지 알기 위해 한단계 더 들어가야 한다
    - 코드가 추상화됨으로써 오는 장점만 있는 게 아니고 이런 단점도 있다
- 기능을 확장할 가능성이 없다면 `구체 클래스를 직접 사용`하고, 향후에 변경이 꼭 필요할 때 `리팩토링`해서 인터페이스를 도입하는 것도 좋은 방법!

> 아무래도 경험이 필요한 부분, 이게 참 미묘하다. 이런걸 잘 고민하고 설계하는게 경험이 많은 좋은 아키텍트

---

## 비즈니스 요구사항:

- 회원
    - 회원은 가입하고 조회할 수 있다
    - 회원은 일반과 VIP 두 가지 등급이 있다
    - 회원 데이터는 자체 DB를 구축할 수도 있고, 외부 시스템과 연동할 수도 있다 (미확정)
- 주문과 할인 정책
    - 회원은 상품을 주문할 수 있다
    - 회원 등급에 따라 할인 정책을 적용할 수 있다
    - 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용 (나중에 변경될 수 있음)
    - 할인 정책은 변경 가능성이 높다. 회사는 아직 기본 할인 정책을 결정하지 못했고, 오픈 직전까지 결정을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수도 있다 (미확정)

다 결정될때까지 개발을 기다릴 수는 없는 일. 앞서 배운 객체지향 설계 방식을 활용해보자

---

특정 인터페이스에 대한 구현체가 하나만 있을 땐 `Impl`이라는 네이밍을 많이 사용한다

객체지향적으로 설계를 잘 해두면 `역할들의 협력 관계를 그대로 재사용할 수 있다`

- 회원 저장소가 MemoryMemberRepository에서 DbMemberRepository로 변경되어도
- 할인 정책이 FixDiscountPolicy에서 RateDiscountPolicy로 변경되어도
- `주문 서비스 구현체를 변경하지 않아도 된다!`
- 요 내용을 굉장히 잘 풀어낸 책이 조영호님의 `객체지향의 사실과 오해`

---

실무에서 할인 관련된 부분은 테스트를 굉장히 많이 한다. 돈을 다루는 부분, 불안한 부분  
경계값 테스트도 많이 해야 함

지금까지 객체지향적으로 설계를 잘 해둔 것처럼 보였지만 사실 아니다  
`OrderServiceImpl`이 `DiscountPolicy` 인터페이스 뿐만 아니라 `FixDiscountPolicy`에도 의존하고 있기 때문에,  
이걸 `RateDiscountPolicy`로 변경하는 순간 `OrderServiceImpl`도 수정해줘야 한다.

기능을 확장해서 변경하면 `클라이언트 코드에도 영향을 준다`  
즉, OCP를 위반한다 (DIP를 위반하고 있었기 때문에 발생한 문제)

> 이 문제를 해결하려면 누군가가 클라이언트인 `OrderServiceImpl`에 `DiscountPolicy` 인터페이스의 구현 객체를 대신 생성하고 주입해줘야 한다

---

## 애플리케이션을 하나의 공연이라 생각해보면..

각각의 인터페이스를 배역이라 생각해볼 수 있다

그러면 실제로 배역을 맡는 배우를 선택하는건 누가 하나?  
이런건 공연을 기획하는 쪽에서 한다. 배우가 상대 배역을 누가 맡을지 고르고 그 사람을 섭외하고 하지는 않는다

`관심사를 분리하자`

`배우`는 본인의 역할인 `배역을 수행하는 것`에만 집중할 수 있어야 한다  
공연을 구성하고, 담당 배우들을 섭외하고, 역할에 맞는 배우를 지정해주는 책임을 담당하는 `공연 기획자`가 필요한 시점  
-> 공연 기획자를 만들고, 배우와 공연 기획자의 책임을 확실히 분리해보자

전체 애플리케이션의 `구체적인 동작 방식을 구성`(Configure)하기 위해,  
`구현 객체들을 생성`하고 `연결`하는 책임을 가지는 별도의 설정 클래스를 도입할 수 있다

`MemberServiceImpl`, `OrderServiceImpl` 에서 구체화(구현 클래스)에 대한 정보를 제거할 수 있다  
추상화(인터페이스)에만 의존하도록 변경해준 뒤, `생성자 주입` 방식을 통해 의존관계를 주입받도록 할 수 있다  
실제 구현 객체들을 생성하고, 필요한 의존 관계들을 연결해주는건 `AppConfig` 클래스의 관심사(역할)다

`배우는 자신의 배역을 수행하는 데 집중해야 한다. 다른 배역에 누가 캐스팅될지 신경쓰는 건 배우의 관심사가 아니다`

`AppConfig`를 도입함으로써 `MemberServiceImpl`, `OrderServiceImpl` 은 실행에만 집중할 수 있게 됐다

\+ 역할과 구현 클래스가 한눈에 들어올 수 있도록 AppConfig 클래스 리팩토링

---

다형성 덕분에 새로운 할인 정책(`RateDiscountPolicy`) 구현 자체는 아무런 문제가 없었다  
그런데 기존의 할인 정책 대신 새로운 할인 정책을 적용하려고 보니, `클라이언트 코드`인 주문 서비스의 코드도 함께 변경해줘야 했다

왜? 주문 서비스가 추상화인 `DiscountPolicy`에만 의존하는게 아니라, 구체화인 `FixDiscountPolicy`도 함께 의존하고 있었기 때문 (DIP 위반)

이걸 해결하려면..  
애플리케이션을 하나의 공연이라 보고, `공연을 기획하고, 담당 배우들을 섭외하고, 배역을 수행할 배우를 지정`해주는 `공연 기획자`를 도입!

`AppConfig`의 책임: 애플리케이션의 동작 방식을 구성하기 위해 `구현 객체들을 생성하고 연결`  
이제부터 클라이언트 객체들은 `자신의 역할을 수행`하는 것만 집중할 수 있다. 각자의 책임이 명확해진 것

객체지향 설계에서 중요한 것은 `역할과 구현을 명확하게 분리`하는 것!

---

### `제어의 역전, IoC (Inversion of Control)`

일반적인 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결하고, 실행한다  
즉, 구현 객체가 프로그램의 제어 흐름을 스스로 조종했다. <- 개발자 입장에서는 자연스러운 흐름

AppConfig를 도입하게 되면, 구현 객체는 자신의 로직을 실행하는 역할만 담당하게 된다.  
프로그램의 제어 흐름은 이제 AppConfig가 가져간다  
ex) `OrderServiceImpl`은 필요한 인터페이스들을 호출해 사용하지만, 실제로 어떤 구현 객체들이 호출될지는 모른다

> 프레임워크 vs. 라이브러리
>
> 프레임워크란 내가 작성한 코드를 제어하고 대신 실행해주는 것 (JUnit처럼)
>
> > 테스트코드 작성할 때를 보면, `@Test` 붙인 메서드의 로직만 작성한다
> > 테스트코드에 대한 제어권은 JUnit이 가져간다. 테스트 프레임워크에서 대신 실행해준다!
> > \+ 자신만의 라이프사이클이 있음 (`@BeforeEach`를 먼저 실행하고 `@Test`를 실행한다던지)
>
> 반면, 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그건 프레임워크가 아니라 라이브러리다

### `의존관계 주입, DI (Dependency Injection)`

`OrderServiceImpl`은 `DiscountPolicy` 인터페이스에 의존한다. 실제로 어떤 구현 객체가 사용될지는 모름

의존관계는 `정적인 클래스 의존 관계` 와, `실행시점(런타임)에 결정되는 동적인 객체(인스턴스)간의 의존관계` 둘을 분리해서 생각해야 한다!!

1. 정적인 클래스 의존관계
    - 클래스의 `import`문만 보고도 의존관계를 쉽게 파악할 수 있다. 즉, 정적인 의존관계는 애플리케이션을 실행하지 않아도 분석할 수 있다
    - 하지만 이런 클래스 의존관계만으로는 런타임에 어떤 객체가 주입될지 알 수 없다
2. 동적인 객체 인스턴스 의존관계
    - 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존관계
    - 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것을 `의존관계 주입`이라 부른다
    - 객체 인스턴스를 생성하고, 그 참조값을 전달함으로써 연결된다
    - 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고도 클라이언트가 호출하는 대상 타입의 인스턴스를 변경할 수 있다
    - `의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고도 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다`

### `IoC 컨테이너, DI 컨테이너`

- AppConfig 클래스처럼 `객체를 생성하고 관리하면서 의존관계를 연결해주는 것`을 `IoC 컨테이너` 또는 `DI 컨테이너` 라고 부른다
- 의존관계 주입에 초점을 맞춰, 최근에는 주로 `DI 컨테이너` 라고 함
- 우리가 지금 배우고 있는 `스프링`도 `DI 컨테이이너`다
- `어셈블러`, `오브젝트 팩토리` 처럼 부르기도 한다

---

## 스프링 컨테이너?

- `ApplicationContext` 를 `스프링 컨테이너`라 부른다
- 스프링 컨테이너는 `@Configuration`이 붙은 `AppConfig` 클래스를 설정(구성) 정보로 사용한다.
    - 여기서 `@Bean`이 붙은 메서드를 모두 실행해서, return된 객체들을 스프링 컨테이너에 등록한다
    - 이렇게 `스프링 컨테이너에 등록된 객체`를 `스프링 빈`이라 부른다
    - 기본적으로는 `@Bean이 붙은 메서드명` 을 `스프링 빈의 이름`으로 사용한다
- 기존에는 개발자가 직접 `AppConfig`를 사용해서 객체를 생성하고 DI를 수행했지만, 이제부터는 `스프링 컨테이너`를 통해 객체들을 사용할 수 있다
- 기존에는 개발자가 필요한 객체를 `AppConfig`에서 직접 꺼냈지만, 이제부터는 `스프링 컨테이너`를 통해 필요한 객체(스프링 빈)를 찾아야 한다.
    - `스프링 빈`은 `applicationContext.getBean()` 메서드를 통해 찾을 수 있다

---

## 스프링 컨테이너의 생성 과정

`ApplicationContext`를 스프링 컨테이너라고 부른다이. 이 `ApplicationContext`는 인터페이스임
스프링 컨테이너를 만들 때, 지금처럼 어노테이션 기반의 자바 설정 클래스(AppConfig)를 사용할 수도 있고 XML을 사용할 수도 있다 (근데 요즘 XML방식은 거의 안 씀)
`AnnotationConfigApplicationContext` 는 `ApplicationContext` 인터페이스의 구현체들 중 하나

> 엄밀하게는 `스프링 컨테이너`를 부를 때 `BeanFactory`와 `ApplicationContext`로 구분해서 이야기하지만,
> `BeanFactory`를 직접 사용하는 경우는 거의 없기 때문에 일반적으로 `ApplicationContext`를 스프링 컨테이너라고 부른다

1. 스프링 컨테이너 생성
    - `new AnnotationConfigApplicationContext(AppConfig.class)`
    - 스프링 컨테이너를 생성할 땐 `구성 정보`를 지정해줘야 함
        - 여기서는 `AppConfig.class`를 구성 정보로 지정해준 것
2. 스프링 빈 등록
    - 스프링 컨테이너는 파라미터로 넘어온 설정 클래스의 구성 정보를 사용해서 스프링 빈을 등록한다
    - `@Config` 클래스를 살펴봄 -> `@Bean`이 붙은 메서드를 모두 실행해서 스프링 빈으로 등록
        - 스프링 빈 이름은 기본적으로 메서드명을 사용한다
        - `@Bean(name="memberService2")` 처럼 빈 이름을 임의로 부여해줄 수도 있지만.. 일반적이지 않다
        - **_`빈 이름은 항상 다른 이름을 부여해야 한다!!`_**
            - 빈 이름이 겹치게 되면.. 다른 빈이 무시되거나, 기존 빈을 덮어쓰거나, 상황에 따라 오류가 발생하기도 한다
            - 문제를 단순하게 해결해야 한다! 빈 이름을 겹치게 만들지 마라
            - 최근 버전 스프링부트의 경우, 빈 이름 충돌이 나면 경고를 날리면서 튕기는게 기본 설정이다
3. 스프링 빈 의존관계 설정 - 준비
4. 스프링 빈 의존관계 설정 - 완료
    - 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입한다 (Dependency Injection)
    - 단순히 자바 코드를 호출하는 것 같지만 차이가 있다! 자세한 차이는 싱글톤 컨테이너에서 설명

> 참고:
> 스프링은 `빈을 생성하는 단계`와 `의존관계를 주입하는 단계`가 나누어져 있다!

---

## 스프링 컨테이너에 등록된 모든 빈을 조회하는 방법

```java
ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
String[] beanDefinitionNames = ac.getBeanDefinitionNames();

for (String beanDefinitionName : beanDefinitionNames) {
    BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

    // ROLE_APPLICATION: 사용자가 직접 등록한 애플리케이션 빈
    // ROLE_INFRASTRUCTURE: 스프링이 내부적으로 사용하는 빈
    if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
        Object bean = ac.getBean(beanDefinitionName);
        System.out.println("name=" + beanDefinitionName + " object=" + bean);
    }
}
```

`ac.getBeanDefinitionNames();` : 스프링 컨테이너에 등록된 모든 빈 이름을 조회함
`ac.getBean();` : 빈 이름으로 스프링 컨테이너에 등록된 스프링 빈 객체를 조회함

`beanDefinition.getRole();` :

- `ROLE_APPLICATION` 은 사용자가 직접 등록한 애플리케이션 빈
- `ROLE_INFRASTRUCTURE` 은 스프링이 내부적으로 사용하는 빈

---

## 스프링 빈 조회 - 동일한 타입이 둘 이상일 때

```java
ApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

// 이러면 예외 발생! (동일한 타입을 가지고 이름만 다른 빈이 있기 때문에 하나를 딱 고를 수 없다)
// MemberRepository memberRepository = ac.getBean(MemberRepository.class);

MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class); // 이름을 지정해주면 실행가능

// 특정 타입의 빈을 모두 조회하려면?
Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
for (String key : beansOfType.keySet()) {
    System.out.println("key = " + key + " value = " + beansOfType.get(key));
}

@Configuration
static class SameBeanConfig {

    @Bean
    public MemberRepository memberRepository1() {
        return new MemoryMemberRepository();
    }

    @Bean
    public MemberRepository memberRepository2() {
        return new MemoryMemberRepository();
    }
}

```

`static class` 를 쓰는 이유?  
클래스의 스코프(노출 범위)를 제한하겠다는 의미!

`이 파일 안에서만 쓰겠다`

스프링 빈 조회 - 상속 관계

빈 조회시 대원칙: `부모 타입의 빈을 조회하면 자식 타입의 빈들이 다 끌려나온다`  
-> 그래서 Object로 조회하면 모든 스프링 빈을 조회한다

---

## `BeanFactory`와 `ApplicationContext`

`BeanFactory`는 스프링 컨테이너의 최상위 인터페이스

- 스프링 빈을 관리하고 조회하는 역할을 담당
- `getBean()` 메서드를 제공

`ApplicationContext`는 `BeanFactory를 상속`받는 인터페이스

- `AnnotationConfigApplicationContext` 같은 구현체를 가짐
- 빈을 관리하고 찾는 기능은 `BeanFactory`가 이미 제공해주는데, 무슨 차이가 있나?
    - 애플리케이션을 개발하려면 빈을 관리하고 조회하는 기능은 물론이고 `수많은 부가 기능`들이 필요하다!
    - 편리한 부가 기능들을 제공함
        - 메시지소스를 활용한 국제화 기능
            - 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력
        - 환경변수
            - 로컬 환경, 개발 환경, 운영 환경 등을 구분해서 처리
        - 애플리케이션 이벤트
            - 이벤트를 발행하고 구독하는 모델을 편리하게 지원
        - 편리한 리소스 조회
            - 파일, 클래스패스, 외부 등에서 리소스를 편하게 조회
- `BeanFactory`를 직접 사용할 일은 거의 없다. 대부분의 경우, 부가기능이 포함된 `ApplicationContext`를 사용

> `BeanFactory` 와 `ApplicationContext` 를 `스프링 컨테이너`라 부른다! (DI 컨테이너)

---

## 스프링 빈 설정 메타정보 - `BeanDefinition`

스프링에서 다양한 설정 방식을 지원할 수 있는 이유?  
`객체지향적으로 역할과 구현을 잘 나눠뒀기 때문!`

스프링 컨테이너는 `BeanDefinition`이라는 설정(구성) 정보에 의존한다

- XML 파일을 읽어서 BeanDefinition을 만들 수도 있다
    - `<bean>` 태그 당 하나씩 `빈 설정 메타정보`가 생성된다
- 자바 코드를 읽어서 BeanDefinition을 만들 수도 있다
    - `@Bean` 메서드 당 하나씩 `빈 설정 메타정보`가 생성된다

\-> 스프링 컨테이너는 이 메타정보를 바탕으로 스프링 빈을 생성한다

조금 더 구체적으로는..

- `AnnotationConfigApplicationContext`는 `AnnotatedBeanDefinitionReader`를 사용해서 `AppConfig.class`를 읽고 `BeanDefinition`을 생성
- `GenericXmlApplicationContext`는 `XmlBeanDefinitionReader`를 사용해서 `appConfig.xml`을 읽고 `BeanDefinition`을 생성

`BeanDefinition`에 들어있는 정보들:

- BeanClassName: 생성할 빈의 클래스 명 (자바 설정파일처럼 팩토리 역할의 빈을 사용하면 없음)
- factoryBeanName: 팩토리 역할의 빈을 사용할 경우 이름, 예) appConfig
- factoryMethodName: 빈을 생성할 팩토리 메서드 지정, 예) memberService
- Scope: 싱글톤(기본값)
- lazyInit: 스프링 컨테이너를 생성할 때 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때 까지 최대한 생성을 지연처리 하는지 여부
- InitMethodName: 빈을 생성하고, 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명
- DestroyMethodName: 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명
- Constructor arguments, Properties: 의존관계 주입에서 사용한다. (자바 설정 처럼 팩토리 역할의 빈을 사용하면 없음)

> BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수도 있지만..
> 실무에서 BeanDefinition을 직접 정의하거나 사용할 일은 거의 없다
>
> 너무 깊이있게 이해하려고 하지 말고, 스프링이 다양한 설정 정보들을 BeanDefinition으로 추상화해서 사용한다는 것 정도만 이해하기

---

## 웹 애플리케이션에서 싱글톤 패턴이 많이 사용되는 이유?

스프링은 `기업용 온라인 서비스`를 지원하기 위해 탄생함 (물론 웹 애플리케이션 말고 다른것도 개발할 수 있지만..)  
`웹 애플리케이션은 보통 여러 고객들이 동시에 요청을 한다`

만약 `AppConfig`처럼 직접 만든 DI 컨테이너를 사용할 경우,  
TPS(Traffic Per Second)가 100이라고 하면 요청을 처리하기 위해 초당 100개의 객체가 새로 생성되고, 동작을 마친 뒤에 소멸하는 것이다  
\-> 말도 안되는 메모리 낭비!!

`싱글톤 패턴, Singleton Pattern`

- 클래스의 `인스턴스 객체가 딱 한개만` 생성되는 것을 보장하는 디자인 패턴
- 즉, 똑같은 객체 인스턴스를 두 개 이상 생성하지 못하도록 막아야 한다
    - private 생성자를 사용해서, 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 함

그런데 요 싱글톤 패턴이 좋은점만 있는 게 아니다

- 싱글톤 패턴을 구현하기 위한 코드 자체가 많이 들어감
- 의존관계상 클라이언트가 구체 클래스에 의존하게 됨 (`구체`.getInstance() 를 호출해야 하기 때문)
    - 즉, `DIP 위반`
- 클라이언트가 구체 클래스에 의존하게 되면서, 자연스럽게 `OCP를 위반할 가능성`도 높아진다
- 테스트하기 어렵다
- 내부 속성을 변경하거나 초기화하기 어렵다
- private 생성자로 인해 자식 클래스를 만들기 어려워진다
    - 유연성이 떨어지는 것

> 그래서 싱글톤 패턴은 안티패턴으로 불리기도 한다..!

---

## 싱글톤 컨테이너

`스프링 컨테이너`는 `싱글톤 패턴의 문제점들을 해결`하면서 `객체 인스턴스들을 싱글톤으로 관리`해준다

- 지금까지 학습한 `스프링 빈`이 바로 `싱글톤으로 관리되는 빈`이다
    - 그렇다고 싱글톤 방식만 지원하는건 아니다! 요청마다 새로운 객체를 생성해서 제공하는 기능도 지원한다

- 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체들을 생성하고 관리하는 기능을 `싱글톤 레지스트리`라고 한다
- 스프링 컨테이너를 활용하면 `싱글톤 패턴의 모든 단점들을 해결`하면서 객체들을 싱글톤으로 유지할 수 있다
    - 싱글톤 패턴 구현을 위한 복잡한 코드가 들어가지 않아도 된다
    - `DIP, OCP, 테스트, private 생성자`로부터 자유롭게 싱글톤 패턴을 사용할 수 있다!

---

## 싱글톤 방식의 주의점

> 싱글톤 패턴을 직접 구현해서 사용하던지, 스프링 컨테이너같은 싱글톤 컨테이너를 사용하던지
> `객체 인스턴스를 하나만 생성`해서 공유하는 `싱글톤` 방식은 중요한 주의점이 있다:
> `여러 클라이언트가 하나의 객체 인스턴스를 공유하기 때문에, 싱글톤 객체는 상태를 가지게 설계하면 안 된다`
>
> > 무상태(Stateless)로 설계해야 한다!!
> >
> > 1. 특정 클라이언트에 의존적인 필드가 없어야 한다
> > 2. 특정 클라이언트가 값을 변경할 수 있는 필드가 없어야 한다
> > 3. 가급적이면 읽기만 가능하도록 설계해야 함
> > 4. 필드 대신, 자바에서 공유되지 않는 `지역변수`, `파라미터`, `ThreadLocal` 등을 사용해야 한다

`스프링 빈은 상태를 가지지 않도록 설계해야 한다!!`

---

## 컴포넌트 스캔과 의존관계 자동 주입

컴포넌트 스캔?  
이름 그대로, `@Component` 어노테이션이 붙은 클래스들을 스캔해서 스프링 빈으로 등록해주는 것

- 이때 스프링 빈 이름은 기본적으로 클래스명을 사용하되, 맨 앞글자만 소문자로 바꿔서 등록한다
- 예를 들어 `MemberServiceImpl` 클래스에 `@Component`가 붙어있다면, 스프링 빈 이름은 `memberServiceImpl`이 됨

`@Configuration` 붙은 클래스가 컴포넌트 스캔의 대상이 된 이유: `@Configuration` 어노테이션도 사실 `@Component` 어노테이션을 포함하고 있기 때문!

`@Component("memberService2")` 처럼 등록될 스프링 빈 이름을 명시적으로 지정해줄 수도 있다

\+ `@Component` 클래스의 생성자에 `@Autowired` 어노테이션이 있으면, 필요한 의존관계들을 스프링이 자동으로 주입해준다  
이때 기본 전략은 `타입이 같은 빈`을 찾아서 주입해주는 것!

`@Component`가 붙은 클래스 뿐만 아니라, 아래 어노테이션이 붙은 클래스도 스프링 빈으로 등록됨 (모두 내부에 `@Component`를 포함하고 있기 때문)

- `@Controller` : 스프링 MVC 컨트롤러에서 사용
    - 스프링 MVC 컨트롤러로 인식
- `@Service` : 스프링 비즈니스 로직에서 사용
    - 별다른 기능은 없지만, 개발자들에게 `핵심 비즈니스 로직`이 있는 비즈니스 계층이라는 정보를 준다
- `@Repository` : 스프링 데이터 접근 계층에서 사용
    - 스프링 데이터 접근 영역으로 인식 + 데이터 계층의 예외를 스프링 예외로 변환해준다
- `@Configuration` : 스프링 설정 정보에서 사용
    - 스프링 설정 정보로 인식 + `CGLIB`을 활용해서 스프링 빈들이 싱글톤으로 관리될 수 있도록 한다

`includeFilters`, `excludeFilters` 옵션을 사용하면 `컴포넌트 스캔 대상을 추가`하거나 `컴포넌트 스캔에서 제외할 대상을 지정`할 수 있지만..  
컴포넌트 스캔 옵션을 건드리기보다는, 가급적 스프링 기본 설정에 맞춰서 잘 쓰는 방향을 권장!

---

## 빈 이름이 겹치면?

1. `자동 빈 등록` vs `자동 빈 등록` 충돌 - `ConflictingBeanDefinitionException` 예외 발생
2. `수동 빈 등록` vs `자동 빈 등록` 충돌 - 수동 빈 등록이 우선권을 가지기 때문에, 자동 등록 빈을 오버라이딩 해 버린다..!
- 물론 의도한 결과일수도 있겠지만, 이런 일이 생기는건 복잡한 설정들이 꼬여서 그런 경우가 대부분
- 그래서 최근 스프링 부트에서는 `수동 빈 등록과 자동 빈 등록이 충돌하면 예외를 발생`시키도록 기본값을 바꿨다

`개발은 혼자 하는게 아니다. 애매한 상황을 만들지 않는 게 제일 좋다!!`

> 애초에 빈 이름이 중복되는 일이 없도록 하자

---

## 다양한 의존관계 주입 방법

1. 생성자 주입
    - 생성자를 통해 의존관계를 주입받는 방법 (지금까지 배운 방식)
    - `생성자가 딱 하나만 있으면 @Autowired 어노테이션을 생략해도 의존관계가 자동으로 주입된다` (스프링 빈의 경우)
    - 생성자 호출 시점에 딱 한번만 호출되는것이 보장됨
    - `불변`, `필수` 의존관계에 사용
2. 수정자 주입 (Setter 주입)
    - 필드 값을 변경하는 수정자 메서드(Setter)를 통해 의존관계를 주입받는 방법
    - `선택`, `변경 가능성이 있는` 의존관계에 사용
        - `@Autowired` 어노테이션은 기본적으로 주입할 대상이 없으면 오류를 발생시킨다
        - 주입할 대상이 없어도 동작하게 하려면 `@Autowired(required = false)`를 사용
3. 필드 주입
    - 코드가 간결해지지만...
    - 스프링같은 DI 컨테이너가 없으면 아무것도 할 수 없다!
        - 즉, 순수한 자바 코드로 작성하는 테스트인 `단위 테스트를 작성할 수 없다`
    - `사용하지 말자!!`
        - 애플리케이션의 실제 코드와 관계 없는 테스트코드에서는 써도 된다
        - 스프링 설정을 목적으로 하는 `@Configuration` 같은 곳에서는 특별한 용도로 사용하기도 한다 (어차피 이건 스프링 뜰때만 사용되니까)
4. 일반 메서드 주입
    - 일반 메서드를 통해 의존관계를 주입받을 수도 있다
    - 일반적으로 이 방식은 잘 사용하지 않는다

> 당연하지만, 스프링 빈이 아닌 `Member` 같은 클래스에서는 `@Autowired`를 사용할 수 없다
> `@Autowired`는 스프링 컨테이너가 관리하는 스프링 빈에서만 동작함!

자동 주입 대상을 옵션으로 처리하려면:

1. `@Autowired(required = false)`
2. `@Nullable`
    - 스프링 프레임워크 7.0 버전부터는 `springframework.lang.Nullable` 대신 `jspecify.annotations.Nullable`이 표준이다!
3. `Optional<>`

---

## 생성자 주입을 선택해라!

예전에는 수정자(세터) 주입, 필드 주입도 많이 활용했지만..  
최근에는 스프링을 포함한 DI 프레임워크 대부분이 `생성자 주입` 방식을 권장한다

1. 불변성
    - 대부분의 의존관계 주입은 `애플리케이션 종료 시점까지 변경할 일이 없다`
    - 오히려 변하지 않아야 하는 경우가 많다
        - 세터 주입을 사용하려면 `setXxx` 메서드를 public으로 열어둬야 함
        - 이러면 누군가 실수로 변경하게 될 위험도 있고, 호출하지 말아야 할 메서드를 public으로 열어두는 것 자체가 좋은 설계 방법이 아니다
    - 생성자 주입 방식을 선택하면 의존관계 주입은 객체 생성 시점에 딱 한번만 일어나는것이 보장되므로, 불변성을 지키는 설계가 가능
2. 누락 위험 방지
    - 생성자 주입 방식을 사용하면 DI 컨테이너를 사용하지 않는 순수한 자바 코드에서도 `주입 데이터를 누락할 경우 컴파일 오류가 발생`한다
    - 필요한 의존관계가 주입되지 않았는데도 컴파일이 되고, 실행 과정에서 `NullPointerException`이 터질 위험이 없다
3. final 키워드 사용 가능
    - 생성자 주입 방식을 선택하면 필드에 `final` 키워드를 붙일 수 있다
    - 이렇게 하면 `생성자에서 값 설정이 누락될 경우 컴파일 오류가 발생`한다
    - `컴파일 오류는 세상에서 제일 빠르고 좋은 오류다`
        - 문제가 있는 상태로 실행이 되는것보다, 컴파일 오류가 발생하는편이 훨씬 낫다

> 생성자 주입 방식이 권장되는 데에는 여러 이유가 있지만, `프레임워크에 의존하지 않고 순수한 자바 언어의 특징을 가장 잘 살릴 수 있는 방법`이기도 하다

---

## 롬복과 최신 트렌드

- 여기서 시작

```java
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

- 생성자가 하나만 있을 땐 `@Autowired` 생략 가능

```java
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

- 롬복의 `@RequiredArgsConstructor`를 사용하면 final이 붙은 필드를 모아서 생성자를 알아서 만들어줌

```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
}
```

---

## 같은 타입의 빈이 여러 개 등록될 경우?

- 이러면 `DiscountPolicy` 타입의 빈이 유일하게 결정되지 않기 때문에 문제가 생긴다

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {...}

@Component
public class RateDiscountPolicy implements DiscountPolicy {...}

@Component
public class OrderServiceImpl {

    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy; // 예외 발생
    }
}
```

1. `@Autowired` 필드명, 파라미터명 매칭 활용
    - 타입 정보를 가지고 빈 매칭을 시도하고, 이때 여러 빈이 있을 경우 `필드 이름`, `파라미터 이름`을 사용해서 빈 매칭을 시도한다 (fallback)

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {...}

@Component
public class RateDiscountPolicy implements DiscountPolicy {...}

@Component
public class OrderServiceImpl {

    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(DiscountPolicy rateDiscountPolicy) {
        this.discountPolicy = rateDiscountPolicy; // 이러면 rateDiscountPolicy 빈이 매칭됨
    }
}
```

2. `@Qualifier` 사용

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {...}

@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {...}

@Component
public class OrderServiceImpl {

    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy; // 이러면 `@Qualifier`끼리 매칭 가능
    }
}
```

3. `@Primary` 사용

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {...}

@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {...}

@Component
public class OrderServiceImpl {

    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy; // `@Primary`인 `rateDiscountPolicy`가 우선순위를 가지게 된다
    }
}
```

> `@Qualifier(mainDiscountPolicy)` 같은 경우, 안에 들어가는 문자열에서 오타가 나는 경우를 잡을 수 없다
> 그래서 `@Qualifier`를 감싸는 커스텀 어노테이션을 만들어 사용하면 이런 문제를 해결할 수 있다!

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {}

////////////////////////////////////////////////////////////////////////////////

@Component
public class FixDiscountPolicy implements DiscountPolicy {...}

@Component
@MainDiscountPolicy // `@Qualifier`를 감싸는 커스텀 어노테이션
public class RateDiscountPolicy implements DiscountPolicy {...}

@Component
public class OrderServiceImpl {

    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(@MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```

---

## 조회한 빈이 모두 필요할때

- `Map`이나 `List` 를 통해 여러 빈을 주입받고, 상황에 맞게 꺼내서 활용할 수 있다

```java
static class DiscountService {

    private final Map<String, DiscountPolicy> policyMap;
    // key: `스프링 빈 이름`, value: `DiscountPolicy 타입으로 조회한 스프링 빈`
    private final List<DiscountPolicy> policies;
    // `DiscountPolicy 타입으로 조회한 모든 스프링 빈`이 들어감

    public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
        this.policyMap = policyMap;
        this.policies = policies;
    }

    public int discount(Member member, int price, String discountCode) {
        DiscountPolicy discountPolicy = policyMap.get(discountCode);

        System.out.println("discountCode = " + discountCode);
        System.out.println("discountPolicy = " + discountPolicy);

        return discountPolicy.discount(member, price);
    }
}
```

---

## 자동 등록 빈, 수동 등록 빈 실무 운영기준

애플리케이션은 크게 `업무 로직`과 `기술 지원 로직`으로 나눌 수 있다

- 업무 로직 빈
    - 웹을 지원하는 `컨트롤러`, 핵심 비즈니스 로직이 있는 `서비스`, 데이터 계층의 로직을 처리하는 `리포지토리`등이 모두 업무 로직이다
    -  보통 `비즈니스 요구사항`을 개발할 때 추가되거나 변경됨
    - `업무 로직은 자동 빈 등록을 적극적으로 사용하는 것이 좋다`
- 기술 지원 빈
    - 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용됨
    - 데이터베이스에 연결하거나, 공통 로그 처리를 하는 등 `업무 로직을 지원하기 위한 기술들`
    - `기술 지원 로직들은 가급적 수동 빈 등록을 사용해서 명확하게 드러내는 것이 좋다`

> 애플리케이션에 광범위하게 영향을 미치는 기술 지원 객체는 수동 빈으로 등록해서 딱! 설정 정보에 바로 나타나게 하는것이 유지보수 하기 좋다!

\+ 업무 로직 빈 중에서도 `다형성을 적극적으로 활용하는 경우` 수동 빈 등록을 고려해보기
- List나 Map을 통해 동일한 타입의 빈을 여러 개 주입받아 사용하는 경우, `자동 빈 등록을 사용하면 어떤 빈들이 주입되는지 파악하기 어려울 수 있다`
- 이럴 경우 아래 코드처럼 `수동 빈 등록`을 사용하는 것을 고려해볼 수 있다 - 자동 빈 등록을 사용하려면, 적어도 같은 패키지에 모아두기
- `핵심은, 딱 보면 이해할 수 있어야 한다!!`
- 개발은 혼자 하는게 아니다

```java
@Configuration
public class DiscountPolicyConfig {

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }

    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

---

## 빈 생명주기 콜백

유저 요청을 받고 DB를 연결하게 되면 느리다. 그래서 애플리케이션 서버는 DB랑 미리 연결을 만들어둔다 (DB 커넥션 풀)  
애플리케이션 시작 시점에 필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 끊어주는 작업을 진행하려면 `객체의 초기화와 종료 작업`이 필요

객체 생성이랑 `초기화`는 다른 개념.  
`객체 안에 필요한 값들이 다 연결돼있고, 사용할 준비가 끝난 상태`

스프링 빈의 이벤트 라이프사이클

1. 스프링 컨테이너 생성
2. 스프링 빈 생성
    - `생성자 주입` 방식을 사용하는 빈은 이 단계에서 의존관계 주입도 같이 일어남
3. 의존관계 주입
4. `초기화 콜백`
5. 사용
6. `소멸 전 콜백`
7. 스프링 종료

> 객체의 생성과 초기화를 분리하자
>
> 객체의 생성자는 `생성`에만 초 집중해야 함
> 생성자는 필수 데이터를 세팅하고 메모리를 할당하는 것 까지만 담당하도록!
>
> 초기화 작업도 결국 그 객체가 동작하는 것 (외부랑 커넥션을 맺는다던지)
>
> > 생성자 안에서 무거운 초기화 작업까지 수행하는것보다, `객체를 생성하는 부분`과 `초기화하는 부분`을 명확하게 나누는 것이 유지보수하기 좋다
>
> 객체 내부에 값을 세팅하는것처럼 단순한 작업의 경우 생성자에서 처리하는 것이 더 나을 수도 있다

---

## 빈 생명주기 콜백을 사용하는 방법

1. `InitializingBean`, `DisposableBean` 인터페이스를 상속받기
    - 스프링 초창기에나 사용하던 방식
    - `내가 코드를 고칠 수 없는 외부 라이브러리에는 사용할 수 없다`는 치명적인 단점이 있다
2. 빈 등록시 `initMethod`, `destroyMethod` 사용
    - 메서드 이름을 자유롭게 지정해줄 수 있다
    - 스프링 빈이 스프링 코드에 의존하지 않는다
    - 코드를 고칠 수 없는 `외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다!!`
    - \+ 종료 메서드 추론 기능이 있다
        - 자바 라이브러리에서는 보통 `close` 나 `shutdown` 이란 이름의 종료 메서드를 사용한다
        - `@Bean`의 `destroyMethod`는 기본값이 `(inferred)` (추론)
        - 이 추론 기능이 `close`나 `shutdown`이라는 이름의 메서드를 자동으로 호출해준다. 종료 메서드를 알아서 추론해 호출함
        - 따라서 스프링 빈으로 등록하면 종료 메서드를 따로 적어주지 않아도 잘 등록한다
        - 추론 기능을 사용하기 싫다면 `destroyMethod=""`처럼 빈 문자열을 지정해주면 된다 (굳이 버그를 만들고싶다면)
3. `@PostConstruct`, `@PreDestroy` 어노테이션 사용
    - 이걸 쓰면 된다
    - 최신 스프링에서 가장 권장하는 방법
    - JSR-250이라는 자바 표준임. 스프링에 종속적인 기술이 아니다
    - 유일한 단점이 `코드를 고칠 수 없는 외부 라이브러리에는 사용할 수 없다`는 것

> `@PostConstruct`, `@PreDestroy`를 사용하자
> \+ 코드를 고칠 수 없는 외부 라이브러리의 경우, `@Bean`의 `initMethod`, `destroyMethod` 써라

---

## `프로토타입 스코프`

- 스프링 컨테이너는 `프로토타입 빈을 생성`하고 `필요한 의존관계를 주입`한다
- 초기화가 필요하다면 초기화도 수행한다
- 이렇게 생성된 프로토타입 빈을 클라이언트에 return하고, 이후에는 해당 빈을 관리하지 않는다
- `프로토타입 빈을 요청받을때마다 매번 새로운 빈이 만들어진다`
- 그래서 해당 빈의 `@PreDestroy`는 호출되지 않는다
- `스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계를 주입하고, 초기화하는것까지만 수행한다`
    - 프로토타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에게 있다
    - 만약 종료 메서드를 호출해줘야 한다면, 프로토타입 빈을 받은 클라이언트에서 종료 메서드를 호출해야 함

싱글톤 빈이랑 프로토타입 스코프 빈을 같이 쓰면 문제가 발생할 수 있다

- 싱글톤 빈은 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관계 주입까지 끝난 뒤 컨테이너 안에서 싱글톤으로 관리된다
- 이 싱글톤 빈이 프로토타입 빈을 주입받아서 사용하는 경우..
    - 싱글톤 빈이 생성되는 시점에 프로토타입 빈을 요청하고, 이때 새로운 프로토타입 빈이 생성 및 초기화된 다음 주입된다
    - 이 싱글톤 빈의 로직에서 내부의 프로토타입 빈이 사용될 경우, `아까 주입받은 프로토타입 빈을 사용한다`
        - 즉, 매번 새로운 프로토타입 빈이 사용되는게 아니라, `프로토타입 빈을 사용하는 의도와는 다르게` 생성 시점에 주입받은 프로토타입 빈을 재사용하게 된다

싱글톤 빈에서 프로토타입 빈을 사용할 때, 매번 새로운 프로토타입 빈을 생성해서 사용하려면?

1. `ApplicationContext`를 주입받고, 프로토타입 빈이 필요할때마다 새로 요청한다
    - 의존관계를 외부에서 주입(Dependency Injection)받는게 아니라, 필요한 의존관계를 찾는(Dependency Lookup) 것
    - 하지만 이렇게 `스프링의 애플리케이션 컨텍스트 전체를 주입`받게 되면..
        - 단위 테스트가 어려워진다
        - 스프링 컨테이너에 종속적인 코드가 된다
    - 컨텍스트 전체를 주입받는 대신, DL 기능만 제공하는 무언가를 사용하면 더 좋을 것
2. ObjectFactory, ObjectProvider 사용
    - 스프링이 제공하는 기능이다

```java
private ObjectProvider<PrototypeBean> prototypeBeanProvider

PrototypeBean prototypeBean = prototypeBeanProvider.getObject(); // 이렇게 하면 매번 새로운 PrototypeBean을 요청할 수 있다. DL만 수행
```

3. JSR-330 Provider 사용
    - 스프링에 종속적인 기능이 아니라, 자바 표준이다
    - build.gradle에 `implementation 'jakarta.inject:jakarta.inject-api'` 를 추가해줘야 사용할 수 있다

```java
// gradle에 `implementation 'jakarta.inject:jakarta.inject-api` 필요
private Provider<PrototypeBean> prototypeBeanProvider

PrototypeBean prototypeBean = prototypeBeanProvider.get(); // 이렇게 하면 매번 새로운 PrototypeBean을 요청할 수 있다. DL만 수행
```

> 이렇게 스프링에서 제공하는 기능이랑 자바 표준 기능이 겹칠 일이 많을텐데, 이럴 땐 대부분의 경우에 `스프링에서 제공하는 기능`을 사용하면 된다

> 웹 애플리케이션을 개발하다 보면, `싱글톤 빈`으로도 대부분의 문제를 해결할 수 있기 때문에 `프로토타입 빈`을 사용할 일은 굉장히 드물다
>
> 하지만 프로토타입 빈과 싱글톤 빈을 함께 사용할 때 발생할 수 있는 문제와 이를 해결할 수 있는 `Provider` 알아두기
> \+ 프로토타입 빈을 사용할 때 뿐만 아니라, Dependency Lookup이 필요할 경우 언제든지 `Provider`를 사용할 수 있다

---

## `웹 스코프`

- request: HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성 및 관리됨
- session: HTTP Session과 동일한 생명주기를 가지는 스코프
- application: 서블릿 컨텍스트(`ServletContext` )와 동일한 생명주기를 가지는 스코프
- websocket: 웹 소켓과 동일한 생명주기를 가지는 스코프

---

- 스프링 애플리케이션이 시작되는 시점에는 웹 요청이 없고, 따라서 `request 스코프`의 빈이 생성될 수 없기 때문에 아래 코드는 오류가 발생한다

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
```

- Provider를 사용하면 `실제 빈 요청`을 지연시킬 수 있다

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject(); // provider를 통해, MyLogger 빈이 실제로 필요할 때 요청하도록 할 수 있다
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
```

- 프록시 빈 객체를 등록해두고 `실제 빈 요청`을 지연시킬 수 있다 (`proxyMode = ScopedProxyMode.TARGET_CLASS` 설정 활용)

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
// 만약 적용 대상이 클래스가 아니라 인터페이스라면 `ScopedProxyMode.INTERFACES`로 설정해야 함
public class MyLogger {...}

////////////////////////////////////////////////////////////////////////////////

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);
        System.out.println("myLogger = " + myLogger.getClass()); // myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0

        myLogger.log("controller test");
        // 사실 클라이언트는 프록시 객체인 `MyLogger$$SpringCGLIB`의 log() 메서드를 호출하는 것
        // 이 프록시 객체는 `request 스코프`의 진짜 MyLogger 객체를 찾고, 진짜 객체의 log() 메서드를 호출해준다

        // 프록시 객체 자체는 싱글톤처럼 등록되어 있으며, `진짜 객체를 찾는 위임 로직`만 수행함
        logDemoService.logic("testId");

        return "OK";
    }
}
```

> Provider를 쓰던지, 프록시를 쓰던지 핵심은 `진짜 객체 조회`를 꼭 필요한 시점까지 `미룬다`는 것
