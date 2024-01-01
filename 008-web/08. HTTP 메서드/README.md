# Http 메서드

## 요약
- REST는 웹의 장점을 최대한 활용할 수 있는 아키텍처이다
- 대표적인 HTTP Method로 GET, POST, PUT, DELETE가 있으며, 총 8개의 메서드가 존재한다
    - GET: 조회 및 읽기
    - POST: 생성
    - PUT: 업데이트
    - DELETE: 삭제

---

## Representational State Transfer(REST)

- 웹의 장점을 최대한 활용할 수 있는 아키텍처
- 당시 웹(HTTP) 설계의 우수성에 비해 제대로 사용되어지지 못하는 모습에 안타까운 로이 필딩(Roy Fielding)이 발표

### 기본 구성

- 자원(Resource): URI
- 행위(Verb): Http Method
- 표현(Representations)

## Http 메서드

- 대표적으로 GET(조회), POST(생성), PUT(수정), DELETE(삭제)
- 전체 목록은 총 8개
  1. `GET`: 서버로부터 데이터 취득
  2. `POST`: 서버에 데이터를 추가, 작성 등
  3. `PUT`: 서버의 데이터를 갱신, 작성 등
  4. `DELETE`: 서버의 데이터를 삭제
  5. `HEAD`: 서버 리소스의 헤더 → 메타 데이터 취득
  6. `OPTIONS`: 리소스가 지원하고 있는 메서드 취득
  7. `PATCH`: 리소스의 일부분 수정
  8. `CONNECT`: 프록시 동작의 터널 접속을 변경

### 멱등성(Idempotence)
- 여러번 수행해도 결과가 같음을 의미 → 호출로 인해 데이터가 변형되지 않음
- Http Method 자료를 찾다보면 종종 언급됨

### GET

```bash
GET /user/1
```

- 데이터를 읽거나 검색할 때 사용되며 수정할 때 사용하지 않음 → Idempotent
- 조회할 데이터에 대한 정보는 URL을 통해 파라미터를 받음
- 성공적으로 이뤄진다면 Body에 XML/JSON과 함께 200(OK) HTTP 응답 코드 반환
- 에러 발생 시 주로 404(Not Found)나 400(Bad Request) 에러 발생
- 캐싱이 가능하여 같은 데이터를 한번 더 조회 시 저장한 값을 사용하여 조회 속도 빨라짐

### POST

```bash
POST /user
body : {date : "example"}
Content-Type : "application/json"
```

- 새로운 리소스 생성 시 사용 → Idemponent X
- 요청 시 Body 값과 Content-type 값을 작성해야 함
- 구체적으론 하위 리소스(부모 리소스의 하위 리소스)들을 생성하는데 사용됨
- 성공적으로 완료하면 201(Created) HTTP 응답과 Body에 저장한 데이터를 반환
- 같은 POST 요청을 반복했을 때 같은 결과물이 나오는 것을 보장하진 않음
- 두 개의 같은 POST 요청을 보내면 같은 정보를 담은 두 개의 다른 resource를 반환할 가능성이 높음

### PUT

```bash
PUT /user/1
body : {date : "update example"}
Content-Type : "application/json"
```

- 리소스 생성 및 업데이트를 위해 서버로 데이터를 보내는데 사용 → Idempotent
- 데이터를 수정하기 때문에 요청 시 Body 값과 Content-Type을 작성해야 함
- URL을 통해 어떤 데이터를 수정할지 파라미터를 받음
- 데이터 조회 성공 시 Body 값에 저장한 데이터 값을 저장하여 성공 응답을 보냄

### DELETE

```bash
DELETE /user/1
```

- 데이터를 삭제하기 때문에 요청 시 Body 값과 Content-Type 값이 비워져있음
- URL을 통해 어떤 데이터를 삭제할지 파라미터를 받음
- 데이터 삭제에 성공 시 Body 값 없이 성공 응답만 보냄

### 상호 비교
#### GET vs. POST
- `GET`과 비교하여 URL에 데이터 정보가 없으므로 조금 더 안전(그래봤자 콘솔에서 다 볼 수 있음)
- `GET` 방식은 캐싱을 하기 때문에 여러번 요청 시 저장된 데이터를 활용하므로 조금 더 빠를 수 있음

#### POST vs. PUT
- 구분해서 사용할 필요가 있음
- `POST`는 새로운 데이터를 계속 생성하기 떄문에 요청마다 데이터를 생성
- `PUT`은 사용자가 데이터를 지정하고 수정하므로 같은 요청을 계속해도 데이터가 계속 생성되는건 아님

#### PUT vs. PATCH
- 구분해서 사용할 필요 있음
- `PUT`은 지정한 데이터를 전부 수정, `PATCH`는 정보의 일부분이 변경됨
- 고로 `PUT`은 멱등, `PATCH`는 멱등하지 않음

# Reference

[Http Method 란? (GET, POST, PUT, DELETE)](https://velog.io/@yh20studio/CS-Http-Method-란-GET-POST-PUT-DELETE)