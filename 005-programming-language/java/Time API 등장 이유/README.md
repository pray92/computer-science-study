# Time API 등장 이유

## 요약

---

## [java.util.Date](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Date.html) 변천사

- JDK 1.0부터 해당 클래스로 날짜와 시간 관련 기능을 제공했음
- 하지만 특정 시점을 날짜가 아닌 밀리초 단위로 표현
- 게다가 1900년을 기준으로 하는 오프셋, 0에서 시작하는 달 인덱스 등 모호한 설계 -> 유용성 저하
  ```java
  Date date = new Date(117,8,21);  // 2017년 8월 21일, 결과 : Thu Sep 21 00:00:00 CET 2017
  ```
  - 결과 또한 직관적이지 않으며, `Date` 클래스의 `toString()`으로 반환되는 문자열을 추가로 활용하기 어려움
  - 기본 시간대가 CET
    - 중앙 유럽 시간대 사용
    - 그렇다고 `Date` 클래스가 자체적으로 시간대 정보를 알고 있는 것도 아님
- 결과적으로 JDK 1.1에서 `Date` 클래스의 여러 메서드를 사장, 대안으로 `java.util.Calendar` 클래스 제공
  - 이 또한 쉽게 에러를 일으키는 설계 문제를 갖고 있었음
  - 1900년도에서 시작하는 오프셋은 없앴으나, 여전히 달의 인덱스는 0부터 시작
  - 그리고 `Calendar` 등장으로 오히려 혼란이 가중됨
  - 게다가 `DateFormat` 같은 일부 기능은 `Date` 클래스에만 작동
  - `DateFormat` 자체도 문제 있음, 무엇보다 Thread에 안전하지 않음 -> 하나의 formatter로 동시 파싱 시 예기치 못한 결과 발생
- 마지막으로 `Date`와 `Calendar`는 가변 클래스 -> 유지보수가 어려움

## Time API wㅔ공 역사
- 부실한 날짜와 시간 라이브러리 때문에 많은 개발자는 `Joda-Time` 같은 3rd-party 날짜 및 시간 라이브러리를 사용
- 결국 JDK 8부터 `Joda-Time`의 많은 기능을 `java.time` 패키지로 추가

# Reference

모던 자바 인 액션