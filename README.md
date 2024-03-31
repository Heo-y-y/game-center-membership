### 사용 기술
- Spring Boot 3.2
- Java 21
- MySQL
- Spring Data JPA
- QueryDSL
- Javascript
- Thymeleaf
- Slack WebHook

### ERD

![스크린샷 2024-03-18 오후 7 07 52](https://github.com/Heo-y-y/development-blog/assets/112863029/cd0b2660-63fd-43ea-861c-ce5e93aed94e)

## 기능 구현 영상

### 회원 추가

https://github.com/Heo-y-y/development-blog/assets/112863029/3c12e5be-b0bb-40ad-8254-f41f96a1c0d0

### 회원 수정

https://github.com/Heo-y-y/development-blog/assets/112863029/3f736182-f611-433b-8f6c-c63e13309dc4

### 검색 기능

https://github.com/Heo-y-y/development-blog/assets/112863029/a8dc61a7-0fae-4089-935a-9dacd275610e

### 유효성 검사

- **회원 유효성**

회원 유효성은 등록과 수정이 같아서 수정에서 구현 영상을 진행했습니다. 이메일 중복을 위해 localhost8586@gmail.com으로 가입을 한 상태에서 찍은 영상입니다.

https://github.com/Heo-y-y/development-blog/assets/112863029/00660372-f520-4cb0-b9d3-1c7039adf569

- **카드 유효성**

https://github.com/Heo-y-y/development-blog/assets/112863029/d90a5add-cd6a-46af-bfb8-529c1e0a6115

### 회원 삭제

https://github.com/Heo-y-y/development-blog/assets/112863029/6b8bf969-7ee2-405c-9158-bd71219f5a7a

### 카드 등록

https://github.com/Heo-y-y/development-blog/assets/112863029/797a9c45-bd8c-4a92-8db2-61cb6c14f871

### 카드 삭제

https://github.com/Heo-y-y/development-blog/assets/112863029/f7d53cab-cc5e-4f3a-bd3c-161d3f7a6deb

### 등급 확인 및 슬랙

https://github.com/Heo-y-y/development-blog/assets/112863029/15c8bb71-0339-46ee-b975-3dd76bd9d604

## 프로젝트를 하면서 배우거나 고민한 점

### 배운 내용

해당 프로젝트를 진행하며, 처음으로 SSR 방식으로 직접 Model로 데이터를 보내 타임리프로 받아서 화면에 렌더링을 하는 작업을 해봤습니다. SSR방식의 활용, 흐름을 배울 수 있었고, 데이터베이스 설계부터 직접 고민하며 기능 구현을 만들고 브라우저에 보여지게 만드는 작업을 통해 좀 더 성장할 수 있었습니다.
또한 인스타클론 프로젝트에서 SSE 방식으로 알림 전송을 하였는데, 슬렉 웹훅을 사용하여 알림 전송을 해보며 새롭게 적용하는 경험을 할 수 있었고, JS Fetch API를 통해 백엔드와 프론트를 같이 작업하며 HTTP에 통신하는 작업을 통해 흐름을 파악할 수 있었습니다.

### 고민한 내용 및 질문

1. 유효성 검증

우선 UI 측면에서 유효성 검증의 메시지를 서버 측에서 받아올 때 기존 작업처럼  dto 클래스에 어노테이션을 적용해 컨트롤러 클래스에서 @Vaild를 통해 검증을 진행하였으나, 화면에 메시지가 정상 출력이 되지 않거나 또는 여러 유효성이 동시에 터지는 경우 해당 메시지들이 전체가 화면에 보이게 되는 상황이 있었습니다. 이러한 부분은 구글링을 통해 서비스 측에서 직접 검증하여 메시지를 보내고, 그 메시지를 JS로 코드로 메시지를 화면에 넘기는 방식으로 작업했습니다. 
하지만 이후에 컨트롤러에서 유효성을 검증하고, 서비스는 비즈니스로직에 집중시키고 코드 가독성을 위해 @Validated과 BindingResult를 사용하여 타임리프에서 제공하는 errors로 처리하는 로직으로 수정하였습니다.

2. JPA

레파지토리에서 직접 작업을 할때 최대한 JPA에서 제공하는 네이밍을 통해 가져오는 방향으로 작업을 했습니다. 조금 복잡한 쿼리 같은 경우에는 @Query를 통해 직접 적용하거나, Querydsl을 통해 진행했습니다.

3. 검색 기능

검색 기능을 구현할 때 Querydsl을 사용해 직접 모델로 값을 넘겨줘서 필터링 작업으로 진행했습니다. 기존 JS로 많은 작업을 했지만, AJAX와 같은 기능으로 일부 기능을 대체할 수 있다고 판단하여 리팩토링으로 한번 적용해 볼 생각입니다.

4. CI/CD

추가로 CI/CD를 구축하여 실제 서비스단에 올려 관리하는 작업을 통해 서비스 구축 경험을 배우고자 합니다.
