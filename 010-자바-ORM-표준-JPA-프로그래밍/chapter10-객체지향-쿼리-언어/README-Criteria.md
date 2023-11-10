# Criteria

- `JQPL`을 Java 코드로 작성하도록 도와주는 빌더 클래스 API
- 문자가 아닌 코드로 작성하기 때문에 컴파일 단계에서 오류를 잡을 수 있음
- 문자 기반의 `JPQL`보다 동적 쿼리를 안전하게 생성 가능
- 단 실제로 개발해보면 코드가 복잡하고 장황함 -> 직관적 이해가 힘듦

## Criteria 기초

- (`criteria.CriteriaEx::query() & queryWithOrderBy()` 참고)

## Criteria 쿼리 생성
- (`criteria.CriteriaEx::createQuery()` 참고)

## 조회
- (`criteria.CriteriaEx::select()` 참고)

## 집합
- (`criteria.CriteriaEx::group(), having()` 참고)

## 정렬
- (`criteria.CriteriaEx::sort()` 참고)

## 조인
- (`criteria.CriteriaEx::join()` 참고)

## 서브 쿼리
- (`criteria.CriteriaEx::subquery()` 참고)

## IN 식
- (`criteria.CriteriaEx::in()` 참고)

## CASE 식
- (`criteria.CriteriaEx::caseFunc()` 참고)
- 
## 파라미터 정의
- (`criteria.CriteriaEx::parameter()` 참고)

## 네이티브 함수 호출
- (`criteria.CriteriaEx::nativeFunc()` 참고)

## 동적 쿼리
- (`criteria.CriteriaEx::dynamic()` 참고)

## 함수 정리

### Expression의 메서드

| 함수명         | JPQL        |
|-------------|-------------|
| isNull()    | IS NULL     |
| isNotNull() | IS NOT NULL |
| in()        | IN          |

### 조건 함수
| 함수명                          | JPQL                     |
|------------------------------|--------------------------|
| and()                        | and                      |
| or()                         | or                       |
| not()                        | not                      |
| equal(), notEqual()          | =, <>                    |
| lt(), lessThan()             | <                        |
| le(), LessTahnOrEqualTo()    | <=                       |
| gt(), greaterThan()          | >                        |
| ge(), greaterThanOrEqualTo() | >=                       |
| between()                    | between                  |
| like(), notLike()            | like, not like           |
| isTrue(), isFalse()          | is true, is false        |
| in(), not(in())              | in, not(in())            |
| isNull(), is NotNull()       | is null, is not null     |
| exists(), not(exists())      | exists, not exists       |
| isEmpty(), isNotEmpty()      | is empty, is not empty   |
| isMember(), isNotMember()    | member of, not member of |

### 스칼라와 기타 함수
| 함수명           | JPQL | 함수명                | JPQL              |
|---------------|------|--------------------|-------------------|
| sum()         | +    | length()           | length            |
| neg(), diff() | -    | locate()           | locate            |
| prod()        | *    | concat()           | concat            |
| quot()        | /    | upper()            | upper             |
| all()         | all  | lower()            | lower             |
| any()         | any  | substring()        | substring         |
| some()        | some | trim()             | trim              |
| abs()         | abs  | currentDate()      | current_date      |
| sqrt()        | sqrt | currentTime()      | current_time      |
| mod()         | mod  | currentTimestamp() | current_timestamp |
| size()        | size |                    |                   |

### 집합 함수
| 함수명                              | JPQL           |
|----------------------------------|----------------|
| avg()                            | avg            |
| max(), greatest()                | max            |
| min(), least()                   | min            |
| sum(), sumAsLong(), sumAsDoubl() | sum            |
| count()                          | count          |
| countDistinct()                  | count distinct |

### 분기 함수

| 함수명          | JPQL     |
|--------------|----------|
| nullif()     | nullif   |
| coalesce()   | coalesce |
| selectCase() | case     |

## Criteria 메타 모델 API

- `Criteria`는 코드 기반이라 컴파일 시점에 오류를 잡을 수 있음
- 하지만 `m.get("age")`에서 `"age"`는 문자열이라 이는 잡을 수 없음
- 따라서 완전한 코드 기반이라 할 수 없음
- 이런 부분까지 코드로 작성하려면 `메타 모델 API`를 사용하면 됨

### 메타 모델 API 적용 후
```java
cq.select(m)
        .where(cq.gt(m.get(Member_.age), 20))
        .orderBy(cb.desc(m.get(Member_.age)));
```
- `m.get(Member_.age)` 처럼 정적인 코드 기반으로 변경된 것을 확인 가능
- 이런 클래스를 표준`CANONICAL` 메타 모델 클래스라 하는데 줄여서 `메타 모델`이라 함
  - 엔티티를 기반으로 만들어야 함
  - 해당 부분은 직접 작성하지 않고, 코드 자동 생성기가 엔티티 클래스를 기반으로 메타 모델 클래스를 만들어 줌
  > `Entity` -> `코드 자동 생성기` -> `메타 모델 클래스`
  ```java
  src/jpabook/domain/Member.class // 원본 코드
  target/generated-sorces/annotations/jpabook/domain/Member_.class // 자동 생성 메타 모델
  ```   
