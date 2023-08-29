# @Transactional

## 요약

- `@Trasactional`은 DB 트랜잭션을 관리하기 위해 사용되는 에너테이션으로, 서비스 메서드 또는 DAO 메서드 같은 비즈니스 로직이 있는 메서드에 적용한다
- Propagation은 비즈니스 로직의 트랜잭션 경계에 대한 정의다
  - `REQUIRED`: 진행 중인 트랜잭션이 존재하면 포함, 없으면 새로 생성해서 실행한다
  - `REQURIED_NEW`: 진행 중인 트랜잭션이 존재하면 일시중지 후에 새로 생성해서 실행한다
  - `NOT_SUPPORTED`: 진행 중인 트랜잭션이 존재하면 일시중지 후에 트랜잭션 없이 비즈니스 로직을 실행한다
  - `SUPPORTS`: 진행 중인 트랜잭션이 있을 경우 그대로 실행하고, 없으면 트랜잭션 없이 비즈니스 로직을 실행한다
  - `MANDATORY`: 진행 중인 트랜잭션이 있을 경우 그대로 실행하고, 없으면 스프링에서 예외를 발생시킨다
  - `NEVER`: 이미 진행 중인 트랜잭션이 있으면 예외를 발생시킨다
  - `NESTED`: 진행 중인 트랜잭션이 있다면 세이브 포인트 지정 후 실행하고, 실행 중 예외 발생 시 세이브 포인트로 roll-back하며 진행 중인 것이 없으면 `REQUIRED`랑 동일하게 작동한다

---

## 트랜잭션
- DB 상태를 일관되게 유지하기 위해 수행되는 작업들의 논리적인 단위
- 성공적으로 완료되거나 실패할 수 있음, 이를 위한 롤백 기능도 제공

### 적용 시 장점
1. 원자성(Atomicity)
   - 트랜잭션 내 모든 작업은 하나의 원자적 작업 단위로 처리
   - 성공 또는 실패하면 커밋되거나 롤백
2. 일관성(Consistency)
   - 완료 시 DB 상태가 일관성있게 유지됨
   - 트랜잭션 전후의 DB 상태는 정의된 일관성 규칙을 준수
3. 격리성(Isolation)
   - 여러 개의 트랜잭션이 동시에 실행되더라도 각 트랜잭션은 다른 트랜잭션 영향을 받지 않음
4. 지속성(Durability)
   - 커밋 시 영구적으로 DB에 저장됨
   - 시스템 장애 또는 중단되더라도 데이터 안정성이 보장됨

## @Transactional

- DB 트랜잭션을 관리하기 위해 사용되는 에너테이션
- 서비스 메서드 또는 DAO 메서드 같은 비즈니스 로직이 있는 메서드에 적용

### 클래스 또는 메서드의 트랜잭션 설정 값
1. Propagation
2. Isolation
3. Timeout
4. Read-only
5. Rollback Conditions

### 내부 pseudo code

```java
// AOP의 대표적인 예시
createTransactionIfNecessary();
try {
    callMethod();
    commitTransactionAfterReturning();
} catch (exception) {
    completeTransactionAfterThrowing();
    throw exception;
}
```
- 스프링은 proxy 또는 바이트 코드를 조작해서 `@Transactional`이 트랜잭션을 관리하게 함

### About SELECT
- `@Transactional`은 주로 쓰기 연산(INSERT, UPDATE, DELETE)을 다룰 때 사용
- **특별한 상황이 아니라면 SELECT는 트랜잭션 범위에서 분리되어 실행될 수 있음**
- 특정 상황에 사용됨
  1. 읽기 작업이 복잡한 경우
     - 읽기 작업이 복잡한 연산을 수행 또는 여러 테이블에 조인하는 경우
     - 이 때 트랜잭션 범위 내에서 일관된 데이터 읽기가 중요할 수 있음
  2. 읽기 작업의 결과가 다른 작업에 영향을 주는 경우
     - 읽기 작업의 결과를 기반으로 후속 작업 수행 시 트랜잭션 내에 읽기 작업 처리하기 유용
  3. 캐시와의 조합
     - 어떤 읽기 작업은 캐시를 사용해 성능 향상
     - 이 경우에도 읽기 작업을 처리하면 캐시 관리 등의 부가적인 작업을 함께 처리 가능
  4. 읽기 작업을 기록하거나 로깅 시
     - 로깅하거나 특정 이벤트 기록 시 트랜잭션을 사용하여 읽기 작업 처리
     - 로깅 또는 기록이 안전하게 처리됨

## Propagation Level

### REQUIRED
```java
@Transactional(propagation = Propagation.REQUIRED)
public void requiredExample(String user) { 
    // ... 
}
```
- 디폴트 옵션
- 진행 중인 트랜잭션이 존재하면 포함, 없으면 트랜잭션을 새로 생성해서 실행

### REQUIRED_NEW
```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void requiresNewExample(String user) { 
    // ... 
}
```
- 스프링은 현재 진행 중인 트랜잭션이 존재하면 일시중지시키고, 트랜잭션을 새로 생성해서 실행
- 이를 정상 수행하기 위해선 활성화된 트랜잭션을 일시중지시키기 위한 `JTATrasactionManager`이 필요

### NOT_SUPPORTED
```java
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public void notSupportedExample(String user) { 
    // ... 
}
```
- 현재 트랜잭션이 존재한다면, 스프링에선 이를 중지시키고 비즈니스 로직이 트랜잭션 없이 실행됨

### 기타(토비스프링에 언급 안된 것들)
#### SUPPORTS
- 활성 중인 트랜잭션이 있을 경우 그대로 실행, 없으면 트랜잭션이 수행되지 않는 비즈니스 로직 실행 
#### MANDATORY
- 활성 중인 트랜잭션이 있다면 그대로 사용, 없으면 스프링에서 예외 발생
#### NEVER
- 이미 활성 중인 트랜잭션이 있으면 예외 발생
#### NESTED
- 활성 중인 트랜잭션을 확인, 있다면 세이브 포인트 지정
- 만약 비즈니스 로직 실행 중에 예외가 발생하면 세이브 포인트로 롤백
- 활성 중인 트랜잭션이 없다면 REQUIRED랑 동일하게 작용

# Reference

[ChatGPT](https://chat.openai.com/share/f4d23e06-cea5-4107-80e5-44b87f131493)

[Transaction Propagation and Isolation in Spring @Transactional | Baeldung](https://www.baeldung.com/spring-transactional-propagation-isolation)