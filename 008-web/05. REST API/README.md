# REST API

REST`Representational State Transfer`의 약자로 웹의 장점을 최대한 활용할 수 있는 아키텍쳐`archietecture`에요.

## 특징
### 1. Uniform Interface
- URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍쳐 스타일

### 2. 무상태성(Stateless)
- HTTP 프로토콜의 특징 중 하나인 연결 해제 후 서버나 클라이언트에 상태 정보를 저장하지 않는 무상태성`stateless`
- 작업을 위한 상태 정보를 저장 및 관리하는 쿠키/세션 정보를 따로 관리하지 않아 API 서버는 들어오는 요청을 단순히 '처리'만 함
- 따라서 서버에서 불필요한 정보를 관리하지 않아 구현이 단순해짐

### 3. 캐시 가능(Cacheable)
- HTTP라는 웹 표준을 그대로 사용하기 때문에, 웹에서 사용하는 기존 인프라를 그대로 활용할 수 있음
- 즉, HTTP의 캐싱 기능 적용이 가능
- HTTP 표준 프로토콜에서 사용하는 last-modifed `E-Tag`를 이용해 캐싱 구현이 가능

### 4. 자체 표현 구조(Self-descriptiveness)
- REST API 메시지만 보고도 쉽게 이해할 수 있는 자체 표현 구조로 되어 있음

### 5. Client-Server 구조
- REST API 서버는 API를 제공하고, 클라이언트는 사용자 인증이나 컨텍스트(세션 정보, 로그인 등)를 직접 관리하는 구조
- 각 역할이 확실히 구분되기 때문에 클라이언트, 서버 간에 개발해야할 내용이 명확해지고, 상호 의존성도 줄어듦

### 6. 계층형 구조
- REST API 서버는 다중 계층으로 구성될 수 있음
- 보안, 로드 밸런싱, 암호화 계층을 추가해 구조 상의 유연성을 둘 수 있음
  - PROXY, 게이트웨이 같은 네트워크 기반의 중간 매체를 사용할 수 있음

> 💡 **프록시(Proxy)**
>- 서버 사이에서 데이터를 전달해주는 서버. 웹 서핑을 비롯한 인터넷 속도의 향승을 목적으로 만들어졌어요.

## REST API 디자인 가이드
### 1. URI는 정보의 자원을 표현해야 한다
```shell
GET /member/delete/1  # 잘못된 예, URI는 자원 표현에 중점을 둬야 함
                      # delete 같은 행위에 대한 표현이 들어가면 안됨

DELETE /member/1     # 올바른 예
```
- 회원 정보를 가져올 땐 GET, 추가 시 행위 표현은 POST 메서드를 사용해 표현해요.
```shell

# 회원 정보 가져오기
GET /members/show/1 # X
GET /members/1      # O

# 회원 추가
GET /members/insert/2 # X, GET 메서드는 리소스 생성에 부적절
POST /members/2       # O
```

### 2. 자원에 대한 행위는 HTTP(GET, POST, PUT, DELETE)로 표현한다

|Method	|역할|
|:---|:---|
|GET	|해당 URI를 요청하면 리소스를 생성|
|POST	|해당 리소스를 조회, 조회 후 해당 도큐먼트에 대한 자세한 정보를 가져옴|
|PUT	|리소스 수정|
|DELETE	|리소스 삭제|

## REST 설계 시 주의점
### 1. 슬래시 구분자(/)는 계층 관계를 나타내는데 사용
    ```shell
    http://restapi.example.com/houses/apartments
    http://restapi.example.com/animals/mammals/whales
    ```
### 2. URI 마지막 문자로 슬래시(/)를 포함하지 않음
   - URI에 포함된 모든 글자는 리소스의 유일한 식별자로 사용되어야 함
   - 즉, URI가 다르다는 것은 리소스가 다른 것, 마찬가지로 리소스가 다르다는 것도 URI가 다른 것
   - 분명한 URI를 만들어 통신해야 하기 때문에 혼동을 주지 않도록 URI 경로 마지막에는 슬래시`/`를 사용하면 안됨
       ```shell
       http://restapi.example.com/houses/apartments/ (X)
       http://restapi.example.com/houses/apartments  (0)
       ```
### 3. 하이픈(`-`)은 URI 가독성을 높이기 위해 사용한다
   - URI를 쉽게 읽고 해석하기 위해, 불가피하게 긴 URI 경로를 사용할 땐 하이픈(-)을 사용해요.

### 4. 언더바(_)는 URI에 사용하지 않는다
   - 글꼴에 따라 다르지만, 언더바_는 보기 어렵거나 문자가 가려지기도 해요.

### 5. URI 경로는 '소문자'가 적합하다
   - URI 경로에 대문자 사용은 지양해야 되요. 대소문자에 따라 다른 리소스로 인식하기 때문이죠.
   - RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자로 구별하도록 규정해요.

### 6. 파일 확장자는 URI에 포함하지 않는다
   - REST API에서는 메시지 바디 내용의 포맷 내용을 나타내기 위한 파일 확장자를 URI에 포함하지 않아요. 대신 Accept 헤더를 사용해요.

## 리소스 간의 관계 표현 방식
- REST 리소스 간에는 연관 관계가 있을 수 있음
    ```shell
    # /리소스명/리소스 ID/관계가 있는 다른 리소스명
    GET : /users/{userid}/devices # (일반적으로 소유 ‘has’의 관계를 표현할 때)
    ```
- 만약에 관계명이 복잡하면 서브 리소스에 명시적으로 표현하는 방법도 있음
    ```shell
    # ex. 사용자가 ‘좋아하는' 디바이스 목록 표현
    # 관계명이 애매하거나 구체적 표현이 필요할 때
    GET : /users/{userid}/likes/devices
    ```

## 자원을 표현하는 Collection과 Document
### Document
- 단순히 문서 또는 객체로 이해하면 됨

### Collection
- 문서 객체들의 집합
```shell
# sports, players라는 컬렉션과 soccer, 13(번 선수)라는 도큐먼트로 표현
# 여기서 중요한 점은 컬렉션은 '복수'로 사용
# 컬렉션을 복수, 도큐먼트를 단수로 지켜준다면 보다 직관적인 URI 설계 가능
http:// restapi.example.com/sports/soccer/players/13
```

# Reference

[Gyoogle님 블로그 - REST API](https://gyoogle.dev/blog/web-knowledge/REST%20API.html)

[NHN Cloud - REST API 제대로 알고 사용하기](https://meetup.nhncloud.com/posts/92)