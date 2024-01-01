# Http Status Code

- 클라우드 환경에서는 HTTP API를 통해 통신하는 것이 일반적
- 이 때 응답 상태 코드를 통해 실패 또는 성공 여부를 확인할 수 있으므로 API 문서를 작성할 때 꼭 알아야할 개념

## Status Code 종류
### 10x : 정보 확인
### 20x : 통신 성공
|상태 코드|이름|의미|
|:---|:---|:---|
|200|OK|요청 성공`GET`|
|201|Create|생성 성공`POST`|
|202|Accepted|요청 접수 O, 리소스 처리 X|
|204|No Contents|요청 성공 O, 내용 없음|

### 30x : 리다이렉트
|상태 코드|이름|의미|
|:---|:---|:---|
|300|Multiple Choice|요청한 URI에 여러 리소스가 존재|
|301|Move Permanently|요청한 URI가 새 위치로 옮겨 감|
|304|Not Modified|요청 URI의 내용 변경 X|

### 40x : 클라이언트 오류
|상태 코드|이름|의미|
|:---|:---|:---|
|400|Bad Request|API에서 정의되지 않은 요청|
|401|Unauthorized|인증 오류|
|403|Forbidden|권한 밖의 접근 시도|
|404|Not Found|요청 URI에 대한 리소스 존재 X|
|405|Method Not Allowed|API에서 정의되지 않은 '메서드' 호출|
|406|Not Acceptable|처리 불가|
|408|Request Timeout|요청 대기 시간 초과|
|409|Conflict|모순|
|429|Too Many Request|요청 횟수 상한 초과|

### 50x : 서버 오류
|상태 코드|이름|의미|
|:---|:---|:---|
|500|Internal Server Error|서버 내부 오류|
|502|Bad Gateway|게이트웨이 오류|
|503|Service Unavailable|서비스 이용 불가|
|504|Gateway Timeout|게이트웨이 시간 초과|

