# Statement vs. PreparedStatement

- 두 객체의 차이점은 캐시 사용 여부이다
- Statement는 매 쿼리 수행 시 분석 -> 컴파일 -> 실행 과정을 거친다
- PreparedStatment는 처음 한번만 3단계를 거치고, 그 다음에 캐시를 담아 실행 과정만 거쳐 재사용한다
- 동일한 쿼리 반복 수행 시 PreparedStatment가 DB 훨씬 적은 부하를 주고, 성능도 좋다

---

- 두 객체는 SQL 쿼리문을 실행할 수 있음
- 가장 큰 차이는 **캐시 사용 여부**

## SQL 실행 단계
1. 쿼리 문장 분석
2. 컴파일
3. 실행

### Statement
```java
String sqlStr = "SELECT name, memo FROM TABLE WHERE name=" + num;
Statement stmt = conn.createStatment();
ResultSet rs = stmt.executeQuery(sqlStr);
```
- 쿼리문을 수행할 떄마다 1~3단계를 모두 거침
- SQL문을 수행하는 과정에서 매번 컴파일하기 때문에 성능 문제 발생
- 실행되는 SQL 문을 확인 가능

### PreparedStatement
```java
String sqlStr = "SELECT name, memo FROM TABLE WHERE name=?";
PreparedStatement pstmt = conn.preparedStatement();
pstmt.setInt(1, num);
ResultSet rs = pstmt.executeQuery(sqlStr);
```
- 컴파일이 미리 되어 있기 때문에 Statement에 비해 성능이 좋음
- 특수 문자를 자동으로 파싱해주기 때문에 SQL Injection 같은 공격 방지
- `?` 부분만 변화를 주어 쿼리문 수행 => 실행되는 SQL 문 파악하기 다소 어려움

#### PreparedStatement 권장 이유
1. 에러 방지: 사용자의 입력 값으로 쿼리문을 실행할 경우, 특수 기호가 들어와도 알아서 파싱
2. 쿼리 반복 수행작업일 경우 용이

# Reference

[[DataBase] Statement와 Prepared Statement 차이점](https://velog.io/@ragnarok_code/DataBase-Statement와-Prepared-Statement-차이점)
