> 2026.04.21 완강
> 
> 쿠키, 세션, 필터, 인터셉터, 예외처리, 타입 컨버터, 파일 부분은 2회독때 마저 정리하기
  
- 검증이란?
  - [링크](#검증이란)
- 검증 로직을 컨트롤러에서 Validator로 분리
  - [링크](#validator-분리)
- Bean Validation? 어노테이션 기반 검증!
  - [링크](#bean-validation-어노테이션-기반-검증)
- DTO, 커맨드 객체 분리
  - [링크](#커맨드-객체-분리)
- API 요청, `@RequstBody` 검증
  - [링크](#api-요청-requstbody와-bean-validation)
  
---
  
## 프로젝트별 주제:
- `thymeleaf-basic`: 타임리프 기본 문법, 코드 스니펫, 레이아웃
- `form`: 체크박스, 라디오 버튼, 셀렉트 박스 등 Form 처리
- `message`: 메시지 소스, 국제화
- `validation`: 수동 검증, `BindingResult`, 오류 메시지, Bean Validation
- `login`: 회원가입, 로그인, 쿠키, 세션, 필터, 인터셉터, ArgumentResolver
- `exception`: 서블릿 예외 처리, 스프링부트 오류 페이지, API 예외 처리
- `typeconverter`: `Converter`, `ConversionService`, `Formatter`
- `upload`: 서블릿 업로드, 스프링 `MultipartFile`, 파일 저장/다운로드
  
---
  
## 타임리프
  
`서버사이드 렌더링 네추럴 템플릿` - 최대한 순수 HTML을 유지하기 때문에, 직접 열어도 레이아웃 안 깨지는게 큰 장점  
  
JSP가 됐든, 타임리프가 됐든 백엔드 개발자라면 서버사이드 렌더링 기술 하나는 알고 있어야 한다고 생각  
최근에 새로 배우려면 타임리프가 제일 좋다  
\+ 스프링 기능이랑 통합이 잘 되어있다  
  
배울 내용이 많지 않음. 변수, 리터럴, 반복 등 기본적인 내용들만 익혀두고, 필요하다면 공식 문서를 찾아보면 됨  
  
> 리터럴? `소스코드 상에서 고정된 값`을 말하는 용어
  
타임리프가 필요하다면 공식문서 찾아보면서 하기. 복습하면서 장기기억으로 가져갈 내용은 아닌 듯 함  
`th:text`, `th:object`, `th:field`, `th:action`, ...  
  
---
  
## 메시지, 국제화
  
메시지: 화면에 표시되는 문구를 코드에서 분리  
국제화: Locale에 따라 메시지를 여러 언어로 제공  
  
타임리프랑 마찬가지로 필요하다면 문서 찾아보기  
  
---
  
## 검증이란?
  
`컨트롤러`의 중요한 역할 중 하나는 `HTTP 요청이 정상인지 검증하는 것`  
  
- 타입 검증
  - 가격, 수량에 문자가 들어가면 검증 오류 처리
- 필드 검증
  - 상품명은 필수, 공백 X
  - 가격은 1000원 이상, 1백만원 이하 (예시)
  - 수량은 최대 9999
- 특정 필드의 범위를 넘어서는 검증
  - 가격\*수량의 합은 10,000원 이상이어야 함
- 검증에는 `클라이언트 검증`과 `서버 검증`이 있음
  - 클라이언트 검증은 조작할 수 있으므로 보안에 취약
    - Postman 같은 HTTP 클라이언트로 API를 호출하면 웹 인터페이스를 건너뛸 수 있다
  - 하지만 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해짐
  - `둘을 적당히 섞어서 사용`하되, `최종적으로 서버 검증은 필수!`
  - API 방식을 사용할 경우 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨줘야 함
  
### 직접 검증
  
컨트롤러에서 직접 검증 로직을 수행하는 방법:  
  
```java
Map<String, String> errors = new HashMap<>();

if (!StringUtils.hasText(item.getItemName())) {
    errors.put("itemName", "상품 이름은 필수입니다.");
}

if (!errors.isEmpty()) {
    model.addAttribute("errors", errors);
    return "validation/v1/addForm";
}
```
  
타임리프에서는 safe call operator를 사용할 수 있음:  
  
```html
th:if="${errors?.containsKey('itemName')}"
```
  
객체가 `null`일 때 그냥 `.`으로 접근하면 NPE가 발생하지만, `?.`는 대상이 `null`이면 예외 대신 `null`을 반환함  
  
직접 검증 방식의 한계:  
  
- 컨트롤러가 너무 많은 일을 함
- 오류 메시지와 검증 로직이 섞임
- 타입 오류처럼 스프링 바인딩 단계에서 발생하는 오류를 직접 다루기 어려움
- 잘못된 사용자 입력값을 유지하기 어려움
  
### BindingResult
  
스프링 MVC는 검증 오류를 `BindingResult`에 담아줌  
  
```java
public String addItem(@ModelAttribute Item item,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {
```
  
`중요`  
  
- `BindingResult`는 검증 대상 `바로 뒤에` 와야 한다
  - `@ModelAttribute Item item` 다음에 `BindingResult`가 와야 `item`의 바인딩/검증 오류를 담을 수 있음
  
필드 오류는 `FieldError`, 글로벌 오류는 `ObjectError`로 추가함  
  
```java
bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000 이상이어야 합니다."));
```
  
`BindingResult`는 자동으로 모델에 함께 넘어가므로 직접 `model.addAttribute()`를 호출해주지 하지 않아도 됨  
  
### 사용자 입력 값 유지
  
사용자가 `price`에 `abc`처럼 숫자가 아닌 값을 입력했을 때, 오류 처리와 별개로 사용자가 입력했던 값을 유지해야 함  
하지만 단순한 `FieldError` 생성자는 오류가 난 값을 보관할 수 없다  
  
이럴 때 `rejectedValue`를 받는 생성자를 사용할 수 있음  
  
```java
new FieldError(
    "item",
    "price",
    item.getPrice(), // 여기!
    false,
    null,
    null,
    "가격은 1,000 ~ 1,000,000 까지 허용됩니다."
)
```
  
타임리프의 `th:field`는 `BindingResult`에 오류가 있으면 실제 필드 값보다 `rejectedValue`를 우선해서 보여줌  
  
### 검증 오류 화면 처리
  
타임리프는 검증 오류를 편하게 보여주는 기능을 제공:  
  
```html
<div th:if="${#fields.hasGlobalErrors()}">
  <p th:each="err : ${#fields.globalErrors()}" th:text="${err}"></p>
</div>

<input th:field="*{itemName}" th:errorclass="field-error" />
<div class="field-error" th:errors="*{itemName}"></div>
```
  
- `#fields.hasGlobalErrors()`: 글로벌 오류가 있는지 확인
- `#fields.globalErrors()`: 글로벌 오류 목록
- `th:errors`: 해당 필드의 오류 메시지 출력
- `th:errorclass`: 해당 필드에 오류가 있으면 CSS class 추가
  
---
  
## Validator 분리
  
위처럼 검증 로직이 컨트롤러에 있으면 컨트롤러가 너무 많은 일을 하게 됨  
컨트롤러 안에 검증 로직이 섞이면, `진짜 로직`을 찾기 위해 한참 내려가야 한다  
  
-> 검증 역할은 별도의 `Validator`로 분리  
  
```java
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
    }
}
```
  
컨트롤러에서는 검증을 위임함  
  
```java
itemValidator.validate(item, bindingResult);
```
  
### WebDataBinder와 @Validated
  
`@InitBinder`로 `WebDataBinder`에 검증기를 등록할 수 있음  
  
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
    dataBinder.addValidators(itemValidator);
}
```
  
이후 검증 대상에 `@Validated`를 붙이면 등록된 Validator가 실행된다  
  
```java
public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult) {
```
  
- `@Validated`는 Validator를 실행시키는 어노테이션
- `WebDataBinder`에 등록된 Validator를 찾아서 검증 로직을 수행
- 어떤 Validator를 사용할지 판단하기 위해 `supports()`를 활용함
- `@InitBinder`는 해당 컨트롤러 클래스에만 영향을 줌
  
---
  
## Bean Validation (어노테이션 기반 검증)
  
Bean Validation은 어노테이션 기반으로 검증 규칙을 선언하는 Java 표준 (JSR-303, JSR-380)  
  
```java
@NotBlank
private String itemName;

@NotNull
@Range(min = 1000, max = 1000000)
private Integer price;

@NotNull
@Max(9999)
private Integer quantity;
```
  
`@NotNull`, `@NotBlank`, `@Size` 등 `Bean Validation 표준`에 정의되어 있어서 모든 구현체에서 사용 가능한 검증 어노테이션도 있고,  
`@Length`, `@Range` 등 `hibernate-validator` 구현체에서 자체적으로 제공하는 `비표준 검증 어노테이션`도 있다  
  
대부분 hibernate 구현체를 사용하긴 하지만, 가급적 표준 어노테이션 사용을 고려하기!  
  
---
  
## `@ScriptAssert`
  
필드 하나만 보고 판단할 수 없는 검증 요구사항도 있다 (ex: 가격과 수량의 곱은 10000 이상이어야 함)  
  
- 이럴 때 `@ScriptAssert`를 사용할수도 있지만, 이런 `복합 검증`은 `자바 코드로 직접 검증`하는게 낫다
  - 복잡한 조건을 어노테이션 문자열로 작성하면 읽기 어려움
  - 컴파일 시점 검증이 어려움
  - 재사용성이 떨어지고 디버깅이 어려워짐
  
- `@ScriptAssert` 사용
```java
@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item
```
  
- 자바 코드로 직접 검증
```java
int resultPrice = item.getPrice() * item.getQuantity();
if (resultPrice < 10000) {
    bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
}
```
  
---
  
## Validation Groups
  
등록과 수정은 검증 요구사항이 다를 수 있다  
  
예를 들어:  
  
- 등록에는 `id`가 없어도 됨
- 수정에는 `id`가 반드시 있어야 함
- 등록에서는 수량 최대값을 제한하지만, 수정에서는 수량 제한을 다르게 둘 수 있음
- ...
  
이럴 때 Bean Validation groups를 사용하면 상황별 검증 그룹을 나눌 수 있다  
  
```java
@NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
private String itemName;

@Range(
        min = 1000,
        max = 1_000_000,
        groups = {SaveCheck.class, UpdateCheck.class}) // `@Range`는 hibernate 구현체에서만 동작함!
@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
private Integer price;

@Max(value = 9999, groups = SaveCheck.class)
@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
private Integer quantity;
```
  
```java
@PostMapping("/add")
public String addItem(
        @Validated(SaveCheck.class) @ModelAttribute Item item, // SaveCheck 그룹의 검증만 수행
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {}

    @PostMapping("/{itemId}/edit")
    public String edit(
            @PathVariable Long itemId,
            @Validated(UpdateCheck.class) @ModelAttribute Item item, // UpdateCheck 그룹의 검증만 수행
            BindingResult bindingResult) {}

```
  
하지만 실무에서는 groups를 잘 사용하지 않는다  
- 복잡도가 올라감
- `등록용 폼 객체`와 `수정용 폼 객체`를 분리하면 `더 단순하고 깔끔하게 해결`할 수 있기 때문
  
---
  
## 커맨드 객체 분리
  
하나였던 폼 객체를 `등록용 폼 정보`를 받는 객체와 `수정용 폼 정보`를 받는 객체로 분리! (커맨드 객체 분리)  
  
> 도메인 객체인 `Item`은 핵심 비즈니스 상태를 표현
> 각각의 폼 객체(SaveForm, UpdateForm)는 화면/API 요청에서 전달받는 값을 표현
>
> 이렇게 상황에 따라 `DTO 또는 커맨드 객체를 분리`하는 것이 `합리적인 판단`!!
  
```java
public class ItemSaveForm {
    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;
}
```
  
```java
public class ItemUpdateForm {
    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    private Integer quantity;
}
```
  
이후 컨트롤러에서 폼 객체를 도메인 객체로 변환해서 저장  
  
```java
Item item = new Item();
item.setItemName(form.getItemName());
item.setPrice(form.getPrice());
item.setQuantity(form.getQuantity());
itemRepository.save(item);
```
  
---
  
## API 요청, `@RequstBody`와 Bean validation
  
HTML Form과 다르게 API 요청에서는 세 가지 경우를 나눠 처리해야 한다:  
  
1. 요청 처리 성공
2. JSON을 객체로 변환하는 단계에서 실패
3. 객체 변환은 성공했지만 검증에서 실패
  
```java
@PostMapping("/add")
public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

    log.info("API 컨트롤러 호출");

    if (bindingResult.hasErrors()) {
        log.info("검증 오류 발생 errors={}", bindingResult);
        return bindingResult.getAllErrors();
    }

    log.info("성공 로직 실행");
    return form;
}
```
  
- `@RequestBody`는 HTTP 메시지 컨버터가 일단 `JSON 전체`를 `객체로 변환`해야 한다
- JSON 문법 오류나 타입 변환 실패로 객체 생성 자체가 안 되면 컨트롤러가 호출되지 않을 수 있음
  - 이런건 `BindingResult`로 처리할 수 없다 -> `@ExceptionHandler` 필요
- 객체 생성 이후 Bean Validation 오류가 발생하면 `BindingResult`로 처리할 수 있다
  
> 실제로 API를 다룰 땐, 컨트롤러마다 BindingResult를 주렁주렁 선언하는 대신  
> `@RestControllerAdvice`에서 `MethodArgumentNotValidException`과 `HttpMessageNotReadableException`을 잡아줌
