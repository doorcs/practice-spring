> 2026.04.03 완강

- 스프링 MVC 전체 구조
  - [링크](#스프링-mvc-전체-구조)
- 핸들러 매핑
  - [링크](#핸들러-매핑과-핸들러-어댑터)
- HTTP 요청 헤더 다루는 방법
  - [링크](#요청-헤더-조회)
- HTTP 요청 파라미터 다루는 방법
  - [링크](#요청-파라미터-조회)
- HTTP 요청 바디 데이터 다루는 방법
  - [링크](#요청-데이터-조회)

---

## 강의소개

웹 학습이 어려운 이유?

1. HTTP 기반 지식이 약해서
2. 자바 백엔드 개발의 역사 자체가 굉장히 오래됨  
   -> 현대 웹 프레임워크가 제공하는 기능들이 `왜 이런식으로 제공되는지` 정확하게 모르고, 단순히 사용법 위주로 익히게 됨 (과거의 문맥을 모름)
3. 스프링 MVC의 복잡성  
   실무 백엔드 개발에 필요한 모든 기능을 제공함. 대신 그만큼 방대하고 학습해야 할 내용도 많음

서블릿 -> JSP -> MVC 패턴 -> MVC 프레임워크 -> 스프링 프레임워크의 탄생  
까지의 과정을 코드로 직접 만들어보면서 단계적으로 알아볼 것  
과거의 어떤 불편함 때문에 이런 기술이 탄생했고, 어떤 점들이 개선됐는지를 이해!

스프링 프레임워크 없이, MVC 프레임워크를 밑바닥에서부터 개발해볼 것  
필요한 기능들을 넣다 보면 자연스럽게 스프링 MVC랑 비슷한 구조가 되어간다

스프링 MVC의 핵심 원리와 구조를 이해 -> 더 깊이있는 백엔드 개발자로 성장!

---

## 웹 서버, 웹 애플리케이션 서버

웹 서버 (Web Server)

- HTTP 기반으로 동작하는 서버
- 정적 리소스 제공 + 기타 부가기능
  - HTML, CSS, JS, 이미지, 영상, ...
- 예시: NGINX, APACHE, ...

웹 애플리케이션 서버 (WAS, Web Application Server)

- 웹서버와 마찬가지로 HTTP 기반으로 동작
- 웹 서버의 기능을 모두 포함한다 (정적 리소스 제공 가능)
- 프로그램 코드를 실행해서 `애플리케이션 로직을 수행`할 수 있다
  - 동적 HTML 생성, HTTP API(JSON) 제공 가능
  - 서블릿, JSP, 스프링 MVC 같은 것들도 모두 WAS 위에서 동작함
- 예시: Tomcat, Jetty, Undertow, ...

웹 서버는 정적 리소스(파일) 제공, WAS는 애플리케이션 로직 실행  
사실 두 용어의 경계가 모호하다

- 웹 서버도 프로그램 실행 기능을 포함하기도 한다
- WAS도 웹 서버의 기능을 제공한다

자바에서는 `서블릿 컨테이너` 기능을 제공하면 WAS

- 서블릿 없이 자바 코드를 실행하는 서버 프레임워크도 있다

`WAS는 애플리케이션 코드 실행에 조금 더 특화되어 있다`

웹 시스템을 구성하려면?  
사실 `WAS`, `DB` 만으로도 시스템을 구성할 수 있다  
WAS는 정적 리소스와 애플리케이션 로직을 모두 제공할 수 있기 때문!

하지만 이런 구조는 WAS가 너무 많은 역할을 담당하게 된다. 서버 과부하 우려가 있다  
\+ 정적 리소스 제공 기능 때문에 가장 비싼 `애플리케이션 로직` 수행이 어려울 수 있다  
\+ WAS가 죽으면(장애가 발생하면) `오류 화면조차도 노출 불가능`해진다

일반적인 웹 시스템 구성: `WEB` + `WAS` + `DB`

- 정적 리소스 배포는 웹 서버에서 관리
  - 정적 리소스만 제공하는 웹 서버는 잘 죽지 않는다!  
    (로직이 들어가지 않고, 그냥 파일을 제공해주는 것이기 때문)
  - 애플리케이션 로직이 동작하는 WAS는 상대적으로 더 잘 죽는다
  - WAS나 DB에 장애가 발생할 경우에도 웹 서버에서 오류 화면(HTML, 정적 리소스)을 제공해줄 수 있다!
- 웹 서버는 `애플리케이션 로직`같은 동적인 처리가 필요할 경우 `WAS`에 요청을 위임
- `WAS`는 중요한 `애플리케이션 로직 처리`를 전담
- 이런 구조를 선택하면 `효율적인 리소스 관리`가 가능해진다
  - 정적 리소스가 많이 호출된다면 웹 서버를 증설
  - 애플리케이션 리소스가 많이 사용된다면 WAS 서버를 증설

---

## 서블릿?

단순한 HTML 폼에서 보내는 `요청 HTTP 메시지`를 처리하는 웹 애플리케이션 서버(WAS)를 직접 구현하려면..?

- 서버 TCP/IP 연결 대기, 소켓 연결
- HTTP 요청 메시지 파싱
- POST 방식, `/save`라는 URL 인식
- Content-Type 확인
- HTTP 메시지 바디 내용 파싱
- 비즈니스 로직 실행
  - 데이터베이스에 저장 요청
- HTTP 응답 메시지 생성
  - HTTP 시작 라인 생성
  - HTTP 헤더 생성
  - 메시지 바디에 들어갈 HTML 생성
- TCP/IP에 응답 전달, 소켓 연결 종료

여기서 의미있는 비즈니스 로직은 `데이터베이스에 정보 저장 요청` 하나뿐인데, 전/후처리 작업이 너무 너무 많다  
-> `서블릿`을 쓰면 비즈니스 로직 외의 것들 처리를 자동화해준다

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        // 애플리케이션 로직 작성
    }
}
```

- urlPatterns에 적힌 URL(/hello)이 호출되면 서블릿 코드가 실행됨
  - HTTP 요청 정보를 편리하게 사용할 수 있는 `HttpServletRequest`
  - HTTP 응답 정보를 편리하게 제공할 수 있는 `HttpServletResponse`
- 개발자는 서블릿이 제공하는 추상화를 통해 HTTP 스펙을 `매우 편리하게` 사용할 수 있다!

구체적으로는..

- HTTP 요청이 들어오면:
  - WAS는 Request, Response 객체를 새로 만들어서 서블릿 객체를 호출하고, 파라미터로 넘겨준다
    - 개발자는 Reqeust 객체에서 HTTP 요청에 대한 정보를 편리하게 꺼내 사용할 수 있다
    - 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력할 수 있다
  - WAS는 Response 객체에 담겨있는 내용들을 바탕으로 HTTP 응답 정보를 생성한다

서블릿 컨테이너?

- 톰캣(Tomcat)처럼 `서블릿을 지원하는 WAS`를 `서블릿 컨테이너`라고 함
- 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 `싱글톤`으로 관리됨
  - 고객의 요청이 올 때 마다 새로운 객체를 계속 생성하는건 비효율적이다
  - 컨테이너 최초 로딩 시점에 서블릿 객체들을 `싱글톤으로` 미리 만들어두고 재활용
  - `모든 고객의 요청`은 `동일한 서블릿 객체 인스턴스`에 접근
  - `공유 변수 사용 주의` - 상태를 가지면 안 된다
  - 서블릿 컨테이너 종료 시 함께 종료됨
- JSP도 서블릿으로 변환되어 사용됨
- 동시 요청 처리를 위한 멀티 쓰레드 처리를 지원한다

---

## 동시 요청 - 멀티 쓰레드

> 백엔드 개발자는 이게 진짜 중요하다!
> 이 개념을 정리 못하면 `트래픽이 많을 때` 어떻게 해결해야 할 지가 어려울 수 있다

쓰레드

- 애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 쓰레드
- 자바의 메인 메서드를 처음 실행하면 `main`이라는 이름의 쓰레드가 실행됨
- 쓰레드가 없다면 자바 애플리케이션 실행 자체가 불가능
- 쓰레드는 한 번에 하나의 코드 라인만 수행함
- 따라서 `동시 처리`가 필요하다면 쓰레드를 추가로 생성해야 함
- 그런데 이 방식에는 문제점이 있다
  - 쓰레드 생성 비용은 `매우 비싸다`
    - 고객의 요청이 올 때 마다 쓰레드를 생성하면, 응답 속도가 굉장히 늦어질 것
  - 쓰레드 간에는 `컨텍스트 스위칭 비용`이 발생한다
  - 생성되는 쓰레드 수에 `제한이 없다`
    - 제한이 없는게 왜 단점?  
      고객 요청이 너무 많이 몰리면, 서버의 CPU, RAM 임계점을 넘어 생성되는 쓰레드 때문에 서버가 죽을 수 있다

- 그래서 `쓰레드 풀` 방식을 쓴다
  - 요청이 올 때 마다 새로 쓰레드를 만드는게 아님
  - 서버 자원을 고려해 적당한 수의 쓰레드를 미리 만들어서 `쓰레드 풀`에 넣어둔다
    - 요청이 들어오면 `요청 처리를 위해 필요한 쓰레드`를 이 `쓰레드 풀에 요청`
      - 쓰레드 풀의 모든 쓰레드가 사용중이여서 남은 쓰레드가 없다면?
      - 특정 숫자만큼 대기하도록 설정하거나, 기다리는 요청을 거절하도록 설정할 수 있다
    - 요청했던 쓰레드를 다 쓰고 나면 쓰레드를 죽이는 대신 `쓰레드 풀에 반납`
  - 쓰레드가 미리 생성되어 있으므로, 쓰레드를 새로 생성하고 종료하는 비용(CPU)이 절약되고 응답 시간도 빨라진다
  - 생성 가능한 쓰레드의 최대치가 있으므로, 새로운 요청이 몰린다 하더라도 기존 요청들이 안전하게 처리될 수 있다

  - 쓰레드 풀 실무 팁
    - WAS의 주요 튜닝 포인트는 최대 쓰레드(max thread) 수
    - 이 값을 너무 낮게 설정하면?
      - 동시 요청이 많으면 서버 리소스는 여유롭지만 클라이언트는 금방 응답 지연
    - 이 값을 너무 높게 설정하면?
      - 동시 요청이 많으면 CPU, RAM 리소스 임계점 초과로 서버 다운
    - 장애 발생시?
      - 클라우드라면 일단 서버부터 늘리고, 이후에 튜닝
  - 그럼 적정 쓰레드 수는 어떻게 찾음??
    - 애플리케이션 로직의 복잡도, CPU, RAM, I/O 리소스 상황에 따라 모두 다르다
    - 성능 테스트
      - 최대한 실제 서비스와 유사하게 성능 테스트를 시도하기
      - 툴: 아파치 ab, 제이미터, nGrinder

- 아무튼, WAS의 핵심은 `멀티 쓰레드 지원`
  - 멀티 쓰레드 관리에 대한 부분은 WAS가 처리
  - `개발자는 멀티 쓰레드 관련 코드를 신경쓰지 않아도 된다`
  - 개발자는 마치 싱글 쓰레드 프로그래밍을 하듯이 편리하게 소스 코드를 개발할 수 있다

`멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용하기!!`

---

## HTML, HTTP API, CSR, SSR

정적 리소스를 제공할 땐, 고정된 HTML 파일, CSS, JS, 이미지, 영상 등을 제공하면 된다

- dir/web/hello.html
- dir/web/hello.css
- dir/web/hello.js
- dir/web/a.jpg

동적인 HTML 페이지가 필요하다면?

- WAS에서 데이터를 처리한 다음, JSP나 Thymeleaf 등을 활용해 동적으로 HTML을 생성해야 한다
  - 서버는 완성된 HTML을 내려줌
  - 클라이언트(웹 브라우저)는 단순히 HTML을 해석하기만 하면 됨

HTTP API 방식?

- HTML이 아니라, `데이터를 전달`
- 주로 JSON 형식을 사용함
  - `{"주문번호": 100, "금액": 5000}` 같은 형태
  - 웹 브라우저 렌더링을 위한 데이터 포멧이 아님 (브라우저에서 호출하면 그냥 데이터만 보인다)
- 다양한 시스템에서 호출할 수 있음
  - `앱`, `웹 클라이언트`, `서버 to 서버`
- 데이터만 주고받고, UI 화면이 필요하다면 클라이언트에서 별도로 처리
  - `서버`, `UI 클라이언트` 간의 접점
    - 앱 클라이언트
    - 웹 브라우저에서 자바스크립트를 통한 HTTP API 호출
    - React, Vue.js 등의 웹 클라이언트
- `서버 to 서버` 통신
  - 주문 서버 - 결제 서버
  - 기업간 데이터 통신

백엔드 개발자가 서비스를 제공하기 위해 고민해야 할 세가지:

1. 정적 리소스를 어떻게 제공할 것인가
2. 동적으로 제공되는 HTML 페이지를 어떻게 제공할 것인가
3. HTTP API를 어떻게 제공할 것인가

서버 사이드 렌더링 (SSR, Server Side Rendering)

- HTML 최종 결과를 서버에서 만들어 웹 브라우저에 전달
- 주로 정적인 화면에 사용
  - 관련 기술: JSP, Thymeleaf 등

클라이언트 사이드 렌더링 (CSR, Client Side Rendering)

- 자바스크립트를 활용해 HTML 결과를 웹 브라우저에서 동적으로 생성해 사용
- 주로 동적인 화면에 사용, 웹 환경을 마치 앱처럼 필요한 부분 부분 변경할 수 있음
  - 구글 지도, Gmail, 구글 캘린더 등
  - 관련 기술: React, Vue.js 등 (웹 프론트엔드 개발)

> React, Vue 를 사용하더라도 CSR, SSR을 동시에 지원하는 웹 프레임워크도 있다
> SSR을 사용하더라도, 자바스크립트를 활용해서 화면 일부를 동적으로 변경할 수도 있다

그럼 `백엔드 개발자 입장에서` UI 기술에 대해 어디까지 알아야 하나?  
(시대에 따라 다르니 참고만)

- 백엔드 - 서버 사이드 렌더링 기술
  - JSP, Thymeleaf (최근에는 스프링이 타임리프를 밀어주고 있기 때문에, JSP보단 타임리프가 낫다)
  - 화면이 정적이고 복잡하지 않을 때 사용
  - 백엔드 개발자는 `서버 사이드 렌더링 기술` 학습 `필수`
- 웹 프론트엔드 - 클라이언트 사이드 렌더링 기술
  - React, Vue.js
  - 복잡하고 동적인 UI 사용
  - 웹 프론트엔드 개발자들의 전문 분야
- 선택과 집중!
  - 백엔드 개발자의 `웹 프론트엔드 기술` 학습은 `옵션`
    - 관심 있으면 해도 된다. 요즘 잘하는 주니어들은 두가지 다 하기도 한다
  - 백엔드 개발자는 서버, DB, 인프라 등등 수많은 백엔드 기술들을 공부해야 한다
  - 웹 프론트엔드도 깊이있게 잘 하려면 숙련에 오랜 시간이 필요하다

---

## 자바 백엔드 웹 기술 역사

과거 기술

- 서블릿 - 1997
  - HTML 생성이 어렵다
- JSP - 1999
  - HTML 생성은 편리해졌지만, 비즈니스 로직까지 너무 많은 역할을 담당
- 서블릿 + JSP 조합의 MVC 패턴을 사용하기 시작
  - 모델, 뷰, 컨트롤러로 역할을 나누어 개발
  - 서로 역할을 쪼개는 것. 핵심은 `비즈니스 로직`과 `화면 렌더링`을 나누는 것
- MVC 프레임워크 춘추전국시대 - 2000년 초 ~ 2010년 초
  - MVC 패턴 자동화, 복잡한 웹 기술을 편리하게 사용할 수 있는 다양한 기능 지원
  - 스트럿츠, 웹워크, 스프링 MVC(과거 버전)
    - 이때 국내에서는 스트럿츠를 굉장히 많이 썼다. 잘 만든 프레임워크
    - 뒷단의 로직은 스프링 코어를 사용하고, 앞단에는 스트럿츠를 쓰는 식으로 많이 개발했음

현재 기술

- `어노테이션 기반`의 `스프링 MVC` 등장
  - `@Controller`
  - MVC 프레임워크 춘추전국 시대 마무리
- `스프링 부트`의 등장
  - 스프링을 편리하게 사용할 수 있도록 도와주는 껍데기?
  - 스프링 부트는 `WAS를 내장`
  - 과거에는 서버에 WAS를 직접 설치하고, 소스 코드를 war로 빌드해서 배포하곤 했음
  - 스프링 부트는 빌드 결과(jar)에 서버가 포함되어 있음  
    -> 빌드 결과물의 배포 과정 단순화
  - 레거시 프로젝트라면 모를까, 현재는 신규 프로젝트라면 스프링 부트 사용이 기본값이다

최신 기술 - 스프링 웹 기술의 분화

- Web Servlet - Spring MVC
  - 서블릿 기반
  - 서블릿 위에 스프링 MVC를 올려서 동작하는 방식
    - HttpServletRequest, HttpServletResponse
  - 멀티쓰레드 지원
- Web Reactive - Spring WebFlux
  - 완전 최신 기술
  - 완전한 `비동기 논 블로킹 처리`
  - 최소한의 쓰레드로 최대 성능을 냄
    - 쓰레드 간 컨텍스트 스위칭 비용 효율화
  - 함수형 스타일로 개발 가능
    - 동시처리 코드 효율화
  - 서블릿 기술을 사용하지 않음
    - Netty 프레임워크 기반

그런데..

- WebFlux는 기술적 난이도가 매우 높음
  - 함수형으로 짜야 함
  - 비동기에 대한 이해도 필요
- 아직은 RDB 지원이 부족함
  - RDB랑 같이 쓰기 어렵다
  - Redis, ElasticSearch, MongoDB 등과 함께 사용
- 일반 `MVC`의 쓰레드 모델도 `충분히 빠르다`
- 실무에서 아직 많이 사용하지는 않음 (전체의 1% 이하)

> 그래서 강의에서는 MVC에 집중할 것

자바 뷰 템플릿의 역사 - HTML을 편리하게 생성하는 뷰 기능

- JSP
  - 속도 느림, 기능 부족
  - 최근에는 스프링 쪽에서 JSP 사용을 `권장하지 않음`
- 프리마커(Freemarker), Velocity(벨로시티)
  - 속도 문제를 해결, 다양한 기능 제공
- 타임리프(Thymeleaf)
  - 네추럴 탬플릿: HTML의 모양을 유지하면서도 뷰 템플릿 적용 가능
  - 스프링 MVC와 강력한 기능 통합
    - 스프링 예제 코드도 타임리프를 사용해 작성됨
  - `최선의 선택`. 단, 성능은 프리마커나 벨로시티가 더 빠르다
    - 근데 이정도 속도 차이가 문제가 될 일은 거의 없다

---

## HttpServletRequest - 개요

HttpServletRequest의 역할?  
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것  
서블릿은 개발자가 HTTP 메시지를 편리하게 사용할 수 있도록 개발자 대신 HTTP 메시지를 파싱해준다  
그리고 그 결과를 `HttpServletRequest` 객체에 담아 제공한다

```http
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=doorcs&age=20
```

- `HttpServletRequest` 객체에서 꺼낼 수 있는 HTTP 요청 메시지의 데이터
  - Start Line
    - HTTP 메서드
    - URL
    - 쿼리 스트링
    - 스키마, 프로토콜
  - 헤더 (Header)
    - 헤더 조회
  - 바디 (Body)
    - form 파라미터 형식 조회
    - message body 데이터를 직접 조회
- `HttpServletRequest` 객체는 추가로 여러 부가기능들을 함께 제공한다
  - 임시 저장소 기능
    - 해당 HTTP 요청이 시작될때부터 응답이 끝날 때 까지 유지되는 임시 저장소를 제공
      - 저장: `request.setAttribute(key, value)`
      - 조회: `request.getAttribute(key)`
  - 세션 관리 기능
    - `request.getSession(create: true)`

> `HttpServletRequest`, `HttpServletResponse`는 결국
> `HTTP 요청`, `HTTP 응답` 메시지를 편하게 사용할 수 있게 도와주는 객체다
>
> 따라서 이 기능들에 대해 깊이있는 이해를 하려면 `HTTP 스펙` 자체에 대한 이해가 필요하다!

---

## HTTP 요청 데이터 - 개요

HTTP 요청 메시지를 통해 클라이언트에서 서버로 메시지를 전달하는 방법?  
주로 아래 세 가지 방법을 사용

1. GET - `쿼리 파라미터` (쿼리 스트링)
   - /url`?username=doorcs&age=27`
2. POST - `HTML Form`
   - `Content-Type: application/x-www-form-urlencoded`
   - 메시지 바디에 쿼리 파라미터 형식으로 담겨 전달됨
     - `username=doorcs&age=27`
   - 회원 가입, 상품 주문 등에서 HTML Form이 사용됨
3. `HTTP 메시지 바디`에 직접 데이터를 담아서 요청

---

## HttpServletResponse - 기본 사용법

HttpServletResponse 객체의 역할?

- `HTTP 응답 메시지 생성`
  - HTTP 응답 코드 지정
  - 헤더 생성
  - 바디 생성
- \+ 다양한 편의 기능 제공
  - Content-Type
  - 쿠키
  - `Redirect`

---

## MVC 패턴 - 개요

하나의 서블릿이나 JSP만으로 `비즈니스 로직`과 `뷰 렌더링`까지 모두 처리하게 되면..

- 너무 많은 역할이 담긴다
- 즉, 유지보수가 어려워진다
  - 비즈니스 로직을 호출하는 부분이 변경될때도, UI 변경이 필요할때도 모두 같은 파일이 수정됨
  - HTML을 살짝 고쳐야 되는 상황인데 그 파일에 수백줄의 자바 코드가 같이 들어있다면?
  - 비즈니스 로직을 하나 고쳐야 하는 상황인데 그 파일에 수천줄의 HTML이 같이 들어있다면?

> 좋은 설계 원칙: `변경 주기가 다르다면 분리해야 한다`

- `UI를 변경하는 일`과 `비즈니스 로직을 변경하는 일`은 `변경의 라이프사이클이 다르다!!`
  - 변경의 라이프사이클이 다른 부분을 하나의 코드로 관리하게 되면 `유지보수에 좋지 않다`

JSP같은 `템플릿 엔진`은 `화면 렌더링`에 최적화되어 있다  
`서블릿`은 `Java 코드 실행`에 최적화되어 있다  
-> 그래서 본인들 역할에 맞는 업무만 담당하도록 하는 것이 가장 효율적이다

MVC 패턴?

- Model, View, Controller
- 하나의 서블릿이나 JSP에서 모든 로직을 수행하는 대신, `컨트롤러`와 `뷰`라는 영역으로 서로의 역할을 분리
  - `웹 애플리케이션`은 보통 이 `MVC 패턴을 사용`한다
  - 컨트롤러
    - `HTTP 요청`을 받아서 `파라미터를 검증`하고, `비즈니스 로직을 실행`함
      - 컨트롤러가 `비즈니스 로직을 직접 수행`할 수도 있지만, 이렇게 되면 `컨트롤러의 역할이 너무 많아`진다
      - 그래서 일반적으로 `비즈니스 로직`은 별도의 `서비스 계층`에서 처리한다
      - `컨트롤러는 비즈니스 로직이 있는 서비스 계층을 호출해주는 역할만 담당!`
    - `뷰`가 화면을 렌더링하기 위해 `필요한 데이터`들을 조회해서 `모델`에 담아줌
  - 모델
    - 뷰에서 출력할 `데이터가 담기는 통`
    - 뷰가 필요한 데이터들이 모두 담겨있음
    - -> 덕분에 `뷰`는 비즈니스 로직이나 데이터 접근에 대해 몰라도 되고, `화면 렌더링에만 집중`할 수 있다
  - 뷰
    - `모델에 담겨있는 데이터`를 활용해서 `화면을 그리는 일에 집중`한다
    - 대부분 HTML을 생성하지만, XML이나 다른 형식의 파일을 생성할 수도 있다

---

## MVC 패턴의 한계?

MVC 패턴을 적용한 덕분에 `컨트롤러`의 역할과 `뷰`의 역할을 명확하게 구분할 수 있다  
특히 `뷰`는 `화면을 그리는 역할`에 충실한 덕분에, 코드가 깔끔하고 직관적이다  
하지만 `컨트롤러`는 코드 중복이 많고, 불필요한 코드도 많이 보인다

- MVC 컨트롤러의 단점
  - 포워드 중복
    - View로 이동하는 코드가 항상 `중복 호출`되어야 한다
    - `request.getRequestDispatcher(viewPath).forward(request, response)`
  - 사용하지 않는 코드
    - `HttpServletRequest`, `HttpServletResopnse`가 사용될 때도 있지만 사용되지 않는 경우도 많다
    - 특히 JSP를 사용할 경우 `HttpServletResponse`는 사용되지 않는다
    - \+ `HttpServletRequest`, `HttpServletResponse`를 사용하는 코드는 `테스트하기 어렵다`
  - 공통 처리가 어렵다
    - 이게 제일 크다
    - 포워드 중복 같은 부분을 `공통 처리로 분리해서 개선`
      - 단순히 공통 기능을 메서드로 분리하면 되는거 아닌가? 싶을 수 있지만,  
        결과적으로 해당 메서드를 호출하는 코드 자체는 중복될 수 밖에 없고,  
        실수로 호출하지 않는 등 문제가 생길 여지가 있다.

> `공통 처리가 어렵다`는 문제를 해결하기 위해서는
> `컨트롤러 호출 전`에 공통 기능을 처리해야 한다.
>
> 소위 `수문장` 역할을 해줄 녀석이 필요하다
> -> 이걸 해결해주는게 `프론트 컨트롤러 패턴` (입구를 하나로)
>
> `스프링 MVC의 핵심`도 이 `프론트 컨트롤러`에 있다!

---

## 프론트 컨트롤러 패턴 소개

컨트롤러 A, 컨트롤러 B, 컨트롤러 C가 있을 때  
모든 컨트롤러에 적용되어야 하는 `공통 로직`이 필요하다면  
각 컨트롤러에 공통 로직을 추가해줘야 한다 <- 굉장히 번거로움, 유지보수 이슈

`프론트 컨트롤러`를 도입하면, 공통 로직 처리가 간편해지고 유지보수성도 좋아진다  
일단 프론트 컨트롤러 서블릿 하나로 클라이언트의 모든 요청을 받고,  
프론트 컨트롤러에서 `상황에 맞는 컨트롤러`를 찾아 호출해준다

> `스프링 웹 MVC`의 핵심도 바로 이 `프론트 컨트롤러 패턴`!
>
> `스프링 웹 MVC`의 `DispatcherServlet`이 프론트 컨트롤러 패턴을 구현하고 있는 것

---

코드를 개선할 때,
구조적인 큰 걸 개선할 때랑, 디테일한걸 개선할 때 는 준위, 레벨이 다르다
일단 `같은 레벨끼리만 개선`하는게 좋다
구조를 개선하는 중이라면 구조적인 것만 개선하고 기존 코드는 최대한 유지시키기
그 다음에, `구조를 바꿨는데 문제가 없다` 하면 `세세한 부분들을 개선`해가는 식으로 진행

한번에 하려고 하면 (구조를 바꾸면서 세세한 로직까지 개선하려고 하면) 짬뽕이 된다
큰 구조개선을 하거나 리팩토링을 할 땐, 구조적인 것만 개선해서 커밋 -> 배포까지 해서 완전히 잘 되는지 확인하고 끝내야 한다
그 다음 단계로 세밀한것들을 하는 식으로 접근하는게 좋다

---

## 스프링 MVC 전체 구조

스프링 MVC도 `프론트 컨트롤러 패턴`으로 구현되어 있다  
스프링 MVC의 프론트 컨트롤러가 바로 `디스패처 서블릿`이다 (DispatcherServlet)  
그리고 이 디스패처 서블릿이 `스프링 MVC의 핵심`이다!

디스패처 서블릿 등록

- `DispatcherServlet`도 부모 클래스에서 `HttpServlet`을 상속받아 사용하고, 서블릿으로 동작한다
  - DispatcherServlet -> FrameworkServlet -> HttpServletBean -> HttpServlet
- 스프링 부트는 `DispatcherServlet`을 서블릿으로 자동 등록하면서 `모든 경로 (urlPatterns = "/")에 대해 매핑`한다
  - 참고로, `더 자세한 경로`가 `우선순위`가 높기 때문에 다른 경로로 등록한 서블릿도 호출할 수 있다

요청 흐름

- 서블릿이 호출되면 `HttpServlet`이 제공하는 `service()`가 호출된다
  - 스프링 MVC는 `DispatcherServlet`의 부모인 `FrameworkServlet`에서 `service()`를 오버라이드 해뒀음
- `FrameworkServlet.service()`를 시작으로 여러 메서드들이 호출되면서 `DispatcherServlet.doDispatch()`가 호출된다
  - `doDispatch()`의 구조

  ```java
  protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

      HttpServletRequest processedRequest = request;
      HandlerExecutionChain mappedHandler = null;
      ModelAndView mv = null;

      // 1. 핸들러 조회
      mappedHandler = getHandler(processedRequest);
      if (mappedHandler == null) {
          noHandlerFound(processedRequest, response);
          return;
      }

      // 2. 조회된 핸들러를 처리할 수 있는 핸들러 어댑터 조회
      HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

      // 3. 핸들러 어댑터 실행 -> 4. 핸들러 어댑터를 통해 핸들러 실행 -> 5. ModelAndView 반환
      mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

      processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
  }

  private void processDispatchResult(
          HttpServletRequest request,
          HttpServletResponse response,
          HandlerExecutionChain mappedHandler,
          ModelAndView mv,
          Exception exception)
          throws Exception {

      // 뷰 렌더링 호출
      render(mv, request, response);
  }

  protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

      View view;
      String viewName = mv.getViewName();

      // 6. 뷰 리졸버를 통해서 뷰 찾기 -> 7. View 반환
      view = resolveViewName(viewName, mv.getModelInternal(), locale, request);

      // 8. 뷰 렌더링
      view.render(mv.getModelInternal(), request, response);
  }
  ```

  - 동작 순서:
    1. 핸들러 조회
       - 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회
    2. 핸들러 어댑터 조회
       - 조회된 핸들러를 실행할 수 있는 핸들러 어댑터를 조회
    3. 핸들러 어댑터 실행
       - 핸들러 어댑터를 실행
    4. 핸들러 실행
       - 실행된 핸들러 어댑터가 실제 핸들러를 실행
    5. ModelAndView 반환(return)
       - 핸들러 어댑터는 핸들러가 반환하는 정보를 `ModelAndView`로 `변환`해서 반환
    6. viewResolver 호출
       - 뷰 리졸버를 찾고 실행
       - JSP의 경우 `InternalResourceViewResolver`가 자동 등록되고 사용됨
    7. View 반환(return)
       - 뷰 리졸버는 뷰의 논리 이름을 실제 이름으로 바꿔주고, 렌더링 역할을 담당하는 뷰 객체를 반환
       - JSP의 경우 `InternalResourceView(JstView)`를 반환하는데, 내부에 `forward()` 로직이 있음
    8. 뷰 렌더링
       - 뷰 객체를 통해 뷰를 렌더링

---

## 핸들러 매핑과 핸들러 어댑터

직접 만든 컨트롤러(핸들러)가 호출되려면 `핸들러 매핑`과 `핸들러 어댑터`가 필요하다!

- 핸들러 매핑
  - 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다
  - ex) `스프링 빈의 이름`으로 핸들러를 찾을 수 있는 `핸들러 매핑`이 필요하다
- 핸들러 어댑터
  - 핸들러 매핑을 통해 찾은 핸들러를 실행해줄 수 있는 핸들러 어댑터가 있어야 한다
  - ex) `Controller` 인터페이스를 실행할 수 있는 `핸들러 어댑터`가 필요하다

> 스프링에서는 이미 개발에 필요한 대부분의 핸들러 매핑과 핸들러 어댑터를 구현해뒀다  
> 그래서 개발자가 직접 핸들러 매핑이나 핸들러 어댑터를 만드는 일은 거의 없다

스프링 부트가 자동으로 등록해주는 핸들러 매핑과 핸들러 어댑터 (일부 생략)

- 핸들러 매핑
  1. `RequestMappingHandlerMapping`
     - 어노테이션 기반의 컨트롤러인 `@RequestMapping`에서 사용
     - `실무에서는 99.9% 이 방식의 컨트롤러를 사용함`
  2. BeanNameUrlHandlerMapping
     - 요청 URL과 일치하는 이름의 스프링 빈을 찾아 매핑해준다
- 핸들러 어댑터
  1. `RequestMappingHandlerAdapter`
     - 어노테이션 기반의 컨트롤러인 `@RequestMapping`에서 사용
     - `실무에서는 99.9% 이 방식의 컨트롤러를 사용함`
  2. HttpRequestHandlerAdapter
     - `HttpRequestHandler` 라는 인터페이스를 처리하는 핸들러 어댑터
  3. SimpleControllerHandlerAdapter
     - `Controller` 인터페이스를 처리하는 핸들러 어댑터 (어노테이션 X, 과거에 사용하던 것)

---

## 로깅 간단히 알아보기

운영 시스템에서는 `System.out.pringln()` 같은 시스템 콘솔을 사용해 필요한 정보들을 출력하지 않고,  
`별도의 로깅 라이브러리`를 사용해 로그를 출력한다

- 로깅 라이브러리
  - 스프링 부트를 사용하면 스프링 부트 로깅 라이브러리 `spring-boot-starter-logging`가 함께 포함된다
  - 스프링 부트 로깅 라이브러리는 기본으로 다음 로깅 라이브러리를 사용한다:
    - SLF4J (인터페이스)
    - Logback (SLF4J의 구현체 중 하나)

- 올바른 로그 사용법
  - `log.debug("data = {}", data)`
    - 로그 출력 레벨이 info일 경우, 아무 일도 발생하지 않는다. 의미 없는 연산이 수행되지 않음!
  - `log.debug("data = " + data)`
    - `이렇게 쓰면 안 된다!!` 문자열 concat 연산 자체는 로그 레벨과 무관하게 항상 수행되기 때문에, 불필요한 서버 부하를 발생시킴

- 로그 사용시의 장점
  - 쓰레드 정보, 클래스 이름 같은 부가 정보들을 함께 볼 수 있다
  - 출력 포맷을 조정할 수 있다
  - 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영 서버에서는 불필요한 로그를 남기지 않는 등 상황에 맞게 조절할 수 있다
  - 성능도 일반 `System.out`보다 좋다 (내부 버퍼링 처리, 멀티쓰레드 최적화 등)
  - `실무에서는 꼭 로그를 사용해야 한다!`

---

## 요청 매핑 (핸들러 매핑)

`@Controller`는 return 타입이 `String`이면 리턴값을 `뷰 이름`으로 인식한다  
그래서 뷰 리졸버를 통해 뷰를 찾고, 뷰가 렌더링된다

`@RestController`는 return 값을 `HTTP 응답 메시지 바디`에 바로 입력한다  
-> 모든 핸들러(컨트롤러 클래스 안의 메서드)에 `@ResponseBody`를 붙여주는 것과 똑같이 동작함

`@RequestMapping("/request-url")`  
일치하는 URL이 호출될 경우 해당 핸들러(메서드)가 실행되도록 매핑해주는 어노테이션  
배열을 활용해서 여러 개의 URL에 매핑시킬 수도 있다: `@RequestMapping({"/hello", "/basic"})`

`@RequestMapping`을 그냥 사용하면 모든 HTTP 메서드에 대해 매핑된다.  
즉, 모든 메서드를 허용하는 셈 (GET, POST, PUT, DELETE, PATCH, HEAD, ...)

- 특정 HTTP 메서드만 허용하고 싶다면
  1. `@RequestMapping(value = "/url", method = "RequestMethod.GET)` 처럼 `method` 명시
     - RequestMethod.GET, RequestMethod.POST, ...
  2. `@GetMapping`, `@PostMapping` 등 `@xxxMapping` 사용
     - 이게 더 직관적이다!

### 잘 사용하지는 않지만...

- 특정 파라미터 조건에 따라 매핑
  - `@GetMaping(value = "/mapping-param", params = "mode=debug")`
    - 요청에 `mode=debug` 파라미터가 있어야 해당 핸들러와 매핑됨
  - 사용 가능한 포맷:
    - `params = "mode"`
    - `params = "!mode"`
    - `params = "mode=debug"`
    - `params = "mode!=debug"`
    - `params = {"mode=debug", "data=good"}`
- 특정 헤더 조건에 따라 매핑
  - `@GetMapping(value = "/mapping-header", headers = "mode=debug")`
    - 요청에 `mode=debug` 헤더가 있어야 해당 핸들러와 매핑됨
  - 사용 가능한 포맷:
    - `headers = "mode"`
    - `headers = "!mode"`
    - `headers = "mode=debug"`
    - `headers = "mode!=debug"`
    - headers는 배열 못쓰나?
- 미디어 타입 조건에 따라 매핑 (요청)
  - `@PostMaping(value = "/mapping-consume", consumes = "application/json")`
    - HTTP 요청 헤더의 `Content-Type` 헤더를 바탕으로 판단
    - 미디어 타입이 `application/json`이어야 해당 핸들러와 매핑됨
  - 사용 가능한 포맷:
    - `consumes = "application/json"`
    - `consumes = "!application/json"`
    - `consumes = "application/*"`
    - `consumes = "*/*"`
- 미디어 타입 조건에 따라 매핑 (응답)
  - `@PostMapping(value = "/mapping-produce", produces = "text/html")`
    - HTTP 요청 헤더의 `Accept` 헤더를 바탕으로 판단
    - 클라이언트에서 `text/html` 타입의 응답을 허용해야 해당 핸들러와 매핑됨
  - 사용 가능한 포맷:
    - `produces = "text/html"`
    - `produces = "!text/html"`
    - `produces = "text/*"`
    - `produces = "*/*"`

---

## 요청 헤더 조회

```java
@GetMapping("/headers")
public String headers(Locale locale, // 로케일 정보를 조회
                      @RequestHeader MultiValueMap<String, String> headerMap, // 모든 헤더를 MultiValueMap 형식으로 조회
                      @RequestHeader("host") String host, // 특정 헤더의 값을 조회. `required` 옵션과 `defaultValue` 옵션을 줄 수도 있음
                      @CookieValue(value = "myCookie", required = false) String cookie // 특정 쿠키의 값을 조회. `MultiValueMap으로 모든 쿠키를 가져올 수는 없음`
                      ) {}

// `MultiValueMap` 은 `스프링 프레임워크에서 제공하는 컬렉션`이다!
// MultiValueMap<Key, Value> 에서 `.get(Key)`의 리턴 타입은 `List<Value>`

// List<String> values = headerMap.get(key); // MultiValueMap<String, String>이므로 `.get()` 결과는 `List<String>`
```

> HTTP 요청 메시지를 통해 클라이언트가 `서버로 데이터를 전달하는 방법`은 3가지:
>
> 1. GET - 쿼리 파라미터
>    - `/url?username=hello&age=20`
>    - HTTP 메시지 바디 없이, URL의 쿼리 파라미터로 데이터를 전달
> 2. POST - HTML 폼
>    - `Content-Type: application/x-www-form-urlencoded`
>    - HTTP 메시지 바디에 `쿼리 파라미터와 동일한 형식`으로 데이터를 담아서 전달
>      - `username=hello&age=20`
> 3. HTTP 메시지 바디에 데이터를 직접 담아서 요청
>    - `HTTP API`에서 주로 사용하고, 데이터 형식은 `JSON`을 많이 사용
>    - `POST`, `PUT`, `PATCH` 메서드에서 사용

## 요청 파라미터 조회

> 엄밀히 따지자면 `@PathVariable`은 핸들러 매핑과 관련된 부분이긴 하지만..

- `@PathVariable`

```java
@GetMapping("/mapping/{userId}") // URL 경로에 `{userId}`처럼 템플릿이 들어갈 수 있다
public String mappingPath(@PathVariable("userId") String data) {} // URL 템플릿의 변수 이름(userId)을 적어주면 메서드의 파라미터명과 달라도 매핑됨
public String mappingPath(@PathVariable String userId) {} // URL 템플릿의 변수 이름과 파라미터의 이름이 같을 경우 괄호 생략 가능

public String mappingPath(String userId) {} // <- @PathVariable 어노테이션 자체를 생략할수도 있지만 권장 X (명확하게 표현하는편이 낫다)

@GetMapping("/users/{userId}/orders/{orderId})
public String mappingPath(@PathVariable String userId,
                          @PathVariable String orderId) {} // URL 템플릿 여러 개를 매핑시킬 수도 있다
```

- `HttpServletRequest.getParameter()`

```java
@RequestMapping("/request-param-v1")
public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String username = request.getParameter("username");
    // 쿼리 파라미터를 쿼리 스트링이라고도 부르는 이유? `값을 모두 문자열로 취급하기 때문!`
    // 그래서 문자열이 아닌 값을 다룰땐 캐스팅(형변환) 필요
    int age = Integer.parseInt(request.getParameter("age"));
    log.info("username = {}, age = {}", username, age);

    response.getWriter().write("ok");
}

// GET /request-param-v1?username=hello&age=20
```

- `@RequestParam`

```java
@ResponseBody // 뷰 리졸버를 거치지 않고, HTTP 응답 바디에 return 값을 직접 채워주도록 하는 어노테이션
@RequestMapping("/request-param-v2")
public String requestParamV2(@RequestParam("username") String memberName, // 요청 파라미터의 key값을 통해 value를 매핑시켜줌
                             @RequestParam("age") int memberAge) {

    log.info("username = {}, age = {}", memberName, memberAge);
    return "ok";
}

@ResponseBody
@RequestMapping("/request-param-v3")
public String requestParamV3(@RequestParam String username, // 요청 파라미터의 key 이름과 파라미터의 이름이 같을 경우 괄호 생략 가능
                             @RequestParam int age) {

    log.info("username = {}, age = {}", username, age);
    return "ok";
}

@ResponseBody
@RequestMapping("/request-param-v4")
public String requestParamV4(String username, int age) { // <- @RequestParam 어노테이션 자체를 생략할수도 있지만 권장 X (명확하게 표현하는편이 낫다)

    log.info("username = {}, age = {}", username, age);
    return "ok";
}

@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(@RequestParam(required = true) String username, // `required = true`일 경우, 요청에 해당 파라미터가 없으면 `400 Bad Request`
                                   @RequestParam(required = false) Integer age) { // `required = false`일 경우, 요청에 해당 파라미터가 없으면 `null`로 채워줌

    // 주의사항!!

    // 1. `int`, `long` 등의 primitive type에는 `null`이 들어갈 수 없다
    //     그래서 `required = false`를 쓰려면 `Integer`, `Long` 같은 wrapper type을 써야 함!
    //
    // 2. `required = true`가 예외를 발생시키는 경우는 `해당 key 자체가 없을 때` 뿐이다!!
    //     즉, `/request-param-required?username=` 같은 요청의 경우 username 변수에 빈 문자열이 들어오고, `예외를 발생시키지 않는다!!`
    log.info("username = {}, age = {}", username, age);
    return "ok";
}

@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(@RequestParam(defaultValue = "guest") String username,
                                  @RequestParam(defaultValue = "-1") int age) {

    // defaultValue는 빈 문자열인 경우에도 적용된다
    // 그래서 `/request-param-default?username=` 처럼 호출하면 username에 기본값인 "guest"가 들어감

    // `defaultValue` 옵션은 문자열만 받는다. 지금처럼 파라미터 타입이 int일 경우 스프링에서 자동으로 "-1"을 -1로 변환해줌
    log.info("username={}, age={}", username, age);
    return "ok";
}
```

- `@ModelAttribute`

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {

    // `@ModelAttribute`의 기능:
    // 1. 빈 객체 생성
    // 2. HTTP 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾음
    // 3. 해당 프로퍼티의 setter 메서드를 호출해 값을 바인딩해줌

    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
    return "ok";
}

public String modelAttributeV1(@RequestParam String username,
                               @RequestParam int age) {

    HelloData helloData = new HelloData();
    helloData.setUsername(username);
    helloData.setAge(age); // 여기까지를 파라미터에 들어가는 `@ModelAttribute Hellodata helloData`에서 대신 처리해주는 셈!

    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
    return "ok";
}

@ResponseBody
@RequestMapping("/model-attribute-v2")
public String modelAttributeV2(HelloData helloData) { // <- `@ModelAttribute` 어노테이션 자체를 생략할 수도 있지만, 권장 X

    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

> 주입받는 파라미터의 어노테이션을 생략할때의 동작:
>
> String, Long, long, int 등 `단순 데이터 타입`: `@RequestParam` 어노테이션의 생략이라 봄
> User, HelloData 처럼 `직접 만든 클래스`: `@ModelAttribute` 어노테이션의 생략이라 봄
>
> \+ 파라미터 이름을 똑같이 맞춰서 `@RequestParam("userId")`같은 어노테이션 괄호를 생략하는것까진 좋은데,
> 어노테이션 자체를 생략하는 방식은 권장되지 않는다 ( 명확하게 표현해주는 편이 훨씬 낫다!! )

> 근데 `쿼리 스트링`이나 `쿼리 파라미터`가 아니라, HTTP 요청 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 어떻게 처리??

---

## 요청 데이터 조회

- `HttpServletRequest` 주입 + `getInputStream()` 메서드 활용

```java
@PostMapping("/request-body-string-v1")
public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ServletInputStream inputStream = request.getInputStream(); // HttpServletRequest에서 Input Stream을 직접 얻어서
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // 문자열로 받아오기 (문자열 인코딩 명시 필요)

    log.info("messageBody = {}", messageBody);

    response.getWriter().write("ok"); // HttpServletResponse 객체의 getWriter() 메서드를 활용하면 응답 데이터를 `HTTP 응답 바디`에 직접 담을 수 있다!
}
```

- `InputStream` 바로 주입받기

```java
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

    // `HttpServletRequest`, `HttpServletResponse` 전체가 필요하지 않다면 `InputStream`, `OutputStream`만 주입받아도 된다!
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("messageBody = {}", messageBody);
    responseWriter.write("ok");
}
```

- `@HttpEntity` 활용
  - 내부적으로 `HttpMessageConverter`를 통해 `HTTP 메시지 바디`와 - `문자열 또는 자바객체` 간 변환 작업을 수행해준다

```java
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {

    String messageBody = httpEntity.getBody(); // 스트림을 직접 다루는 대신, 추상화된 `HttpEntity` 객체를 활용할 수 있다!
    log.info("messageBody = {}", messageBody);

    return new HttpEntity<>("ok"); // `HTTP 응답 데이터`를 다루는데도 사용 가능
}
```

- `@RequestBody`, `@ResponseBody` 활용
  - 내부적으로 `HttpMessageConverter`를 통해 `HTTP 메시지 바디`와 - `문자열 또는 자바객체` 간 변환 작업을 수행해준다

```java
@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody) {

    log.info("messageBody = {}", messageBody);

    return "ok";
}
```

---

## 요청 데이터 조회 - JSON

1. `HttpServletRequest`의 `getInputStream()`으로 스트림을 얻어온 다음 `ObjectMapper.readValue()` 사용
2. `@RequestBody`를 통해 HTTP 응답 바디의 내용을 문자열로 받고, `ObjectMapper.readValue()` 사용
3. `@RequestBody`로 클래스를 주입받도록 해서, `HttpMessageConverter`가 객체 변환 과정을 대신 처리해주도록 하기
   - 물론 `HttpEntity`를 사용할 수도 있는데, 굳이?
   - \+ `HttpMessageConverter`는 요청 뿐만 아니라 응답(return)에도 활용할 수 있다!!

```java
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        // HTTP 메시지 컨버터 (MappingJackson2HttpMessageConverter)가 v2에서 했던 작업을 대신 처리해준다!
        // HTTP 요청 바디의 내용을 읽어야 하기 때문에, `@RequestBody` 어노테이션을 생략하면 안 된다! (생략하면 `@ModelAttribute`로 동작)
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

        // `HttpEntity`를 직접 사용할수도 있다
        // 이때 메시지 컨버터는 메서드의 파라미터를 주입해주는 시점에 동작한다! `.getBody()`는 이미 변환이 완료되어 있는 객체를 꺼내오는 것
        HelloData data = httpEntity.getBody();
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());

        // HTTP 메시지 컨버터는 입력 뿐만 아니라 반환(return)에도 적용된다!
        return data;
    }
}
```
