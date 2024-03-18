### ERD

![스크린샷 2024-03-18 오후 7 07 52](https://github.com/Heo-y-y/development-blog/assets/112863029/cd0b2660-63fd-43ea-861c-ce5e93aed94e)

## 기능 구현 영상

### 회원 추가

https://github.com/Heo-y-y/development-blog/assets/112863029/3c12e5be-b0bb-40ad-8254-f41f96a1c0d0

### 회원 수정

https://github.com/Heo-y-y/development-blog/assets/112863029/3f736182-f611-433b-8f6c-c63e13309dc4

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

## 과제를 하면서 배우거나 고민한 점

### 배운 내용

스플랩의 과제를 진행하며 처음으로 SSR 방식으로 직접 Model로 데이터를 보내 타임리프로 받아서 화면에 렌더링을 하는 작업을 해봤습니다. 과제를 통해 SSR방식의 활용, 흐름을 배울 수 있었고, 데이터베이스 설계부터 직접 고민하며 기능 구현을 만들고 브라우저에 보여지게 만드는 작업을 통해 좀 더 성장할 수 있었습니다. 좋은 과제 내주셔서 감사합니다.

프로젝트를 진행하며 요구사항을 잘못 파악하여 어드민의 관리로 인하여 회원이 카드를 생성하는 작업으로 진행을 했었습니다. 하지만 다시 요구사항을 확인하여 충족시키기 위해 rollback을 진행하여 작업을 했습니다. 자세한 이슈 내용은 [이슈 페이지](https://github.com/Heo-y-y/game-membership/issues/17)에 작성해놨습니다. 

### **고민한 내용**

1. 유효성 검증

우선 UI 측면에서 유효성 검증의 메시지를 서버 측에서 받아올 때 기존 작업처럼  dto 클래스에 어노테이션을 적용해 컨트롤러 클래스에서 @Vaild를 통해 검증을 진행하였으나, 화면에 메시지가 정상 출력이 되지 않거나 또는 여러 유효성이 동시에 터지는 경우 해당 메시지들이 전체가 화면에 보이게 되는 상황이 있었습니다. 이러한 부분은 구글링을 통해 서비스 측에서 직접 검증하여 메시지를 보내고, 그 메시지를 JS로 코드로 메시지를 화면에 넘기는 방식으로 작업했습니다. 토비님과 은진님께 더 좋은 방향을 듣고 싶습니다.

1. JPA

레파지토리에서 직접 작업을 할때 최대한 JPA에서 제공하는 네이밍을 통해 가져오는 방향으로 작업을 했습니다. 조금 복잡한 쿼리 같은 경우에는 @Query를 통해 직접 적용하거나, Querydsl을 통해 진행했는데, 토비님과 은진님께서는 어떠한 기준으로 작업을 하시는지 궁금합니다.

1. WebHook

요구사항에 보내주신 슬랙 API를 진행했으나 invalid_token 에러가 떠서 해당 API로는 진행을 못하고, 저의 슬랙으로 작업을 했습니다. 혹시 제공해주신 API가 되는 것이 맞으면 저의 구현 방법에서 고쳐야할 점이나 방향성을 듣고 싶습니다.

1. 요구사항

보내주신 과제의 요구사항을 최대한 동일하게 진행하려 했는데, 의도하신 바와 다른 점이나 좀 더 좋은 방향이 있으면 말씀해주시면 감사하겠습니다.
