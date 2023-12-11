# @EnableRedisHttpSession

## 요약
- @EnableRedisHttpSession는 스프링 세션 프로젝트에서 제공하는 어노테이션으로, 스프링 어플리케이션에 Redis가 지원하는 분산 세션을 사용할 수 있게 해준다
- 어플리케이션 메모리 대신 Redis 메모리를 사용하게 되므로 어플리케이션 수평 확장에 유용하고 모든 인스턴스에서 세션 데이터가 일관되게 유지된다

---
## @EnableRedisHttpSession
- 스프링 세션 프로젝트에서 제공하는 어노테이션
- 스프링 어플리케이션에 Redis가 지원하는 분산 세션을 사용할 수 있게 해줌
- 어플리케이션 메모리 대신 Redis 메모리를 사용하게 되므로 어플리케이션 수평 확장에 유용하고 모든 인스턴스에서 세션 데이터가 일관되게 유지됨

## 작동 방식

### @EnableRedisHttpSession
- 스프링은 `@EnableRedisHttpSession` 어노테이션을 인식 후 Redis와 연결을 맺어 세션을 관리할 `RedisConnectionFactory`와 `RedisTemplate`을 인식
- Redis 저장소 커넥션을 처리할 준비를 함

### RedisIndexedSessionRepository 빈 생성
- 스프링 세션을 관리하는 빈, `RedisIndexedSessionRepository`가 생성됨
- `SessionRepository`를 구현한 `RedisIndexedSessionRepository`가 빈으로 생성됨
- 이 리포지토리가 세션 데이터에 대한 CRUD 책임을 이어 받음

### SessionRepositoryFilter 등록
- `SessionRepositoryFilter`가 어플리케이션에 등록됨
- `SessionRepositoryFilter`는 `HttpSession` 구현을 Redis-backend 세션 구현으로 대체함
- 요청을 가로채고 Redis로부터 세션 데이터를 받아와 현재 요청과 연결함

### 직렬화와 역직렬화
- 스프링은 Java 데이터를 기본적으로 JDK 직렬화 매커니즘을 통해 직렬화하여 Redis에 저장
- 데이터를 다시 가져올 때는 JDK 역직렬화 매커니즘을 통해 역직렬화하여 Redis에서 Java로 가져옴
- 원한다면 다른 직렬화/역직렬화 매커니즘을 통해 데이터를 주고 받을 수 있음

### 세션 만료
- 기본적으로 30분간 세션 데이터가 쓰이지 않으면 만료됨

#### 만료시간 적용
- `@EnableRedisHttpSession`에 `maxInactiveIntervalInSeconds` 값을 수정해서 설정
  ```java
  @Configuration
  @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) // 1 hour
  public class SessionConfig {
  }
  ```
- `application.yml`에 `spring.session.timeout` 속성 값 지정
  ```yaml
  spring:
    session:
      timeout: 360000
  ```
  
# Reference
[@EnableRedisHttpSession 은 어떻게 동작하는가? (feat. 스프링 세션에 레디스 활용하기)](https://jake-seo-dev.tistory.com/491)