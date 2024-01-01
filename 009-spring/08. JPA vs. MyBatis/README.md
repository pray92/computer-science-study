# JPA vs. MyBatis

## 요약

- MyBatis
    - 자바의 관계형 데이터베이스 프로그래밍을 쉽게할 수 있게 도와주는 Persistence Framework이다
    - Persistence Framework는 DB와 연동되는 시스템을 빠르게 개발하고 안정적 구동을 보장해주는 프레임워크이다
    - XML만을 이용한 SQL문 설정, Annotation/Interface만을 사용한 SQL문 설정, XML과 Interface만을 사용한 SQL문 설정 방법이 있다
- JPA(Java Persistence API)
    - Java ORM 기술에 대한 API 표준 명세이다
    - ORM(Object Relation Mapping)은 RDB 테이블을 객체지향적으로 사용하게 해주는 기술이다
    - 단순한 명세이기 떄문에 JPA만 가지고는 어떤 구현 기술을 사용할 수 없으며, hibernate 같은 구현체를 사용해야 한다
- JPA를 많이 사용하는 추세다
    - 성능 이슈가 발생할 수록 러닝 커브가 존재하고 복잡한 동적 쿼리에는 MyBatis보다 성능이 떨어진다
    - 하지만 QueryDSL 오픈 소스로 이를 극복할 수 있고, 러닝 커브 높은건 배우면 그만이다

---

## MyBatis
- Java의 관계형 데이터베이스 프로그래밍을 쉽게 할 수 있게 도와주는 Persistence Framework

> 💡 **Persistence Framework**<br>
> - Persistence : 애플리케이션을 종료하더라도 사라지지 않는 데이터의 특성
> - Persistence Framework : 데이터베이스와 연동되는 시스템을 빠르게 개발하고 안정적인 구동을 보장해주는 프레임워크

- 기존 자바에선 DB 프로그래밍을 위해 JDBC를 제공
- 직접 DB 생성 → Statement 생성 및 쿼리 수행 → 결과 값(ResultSet) 처리

```java
public void insertUser(User user){
    String query = " INSERT INTO user (email, name, pw) VALUES (?, ?, ?)";

    PreparedStatement preparedStmt = conn.prepareStatement(query);
    preparedStmt.setString (1, user.getEmail());
    preparedStmt.setString (2, user.getName());
    preparedStmt.setString (3, user.getPW());

    // execute the preparedstatement
    preparedStmt.execute();
}
```

- 이러한 방식은 코드가 길어지고 반복됨
- SQL 변경이 필요할 때도 자바 프로그램을 수정하기 때문에 유연성이 좋지 못함
- MyBatis는 JDBC의 단점을 개선하여 개발자가 작성한 SQL 명령어와 자바 객체를 매핑해주는 기능 제공
- 기존에 사용하던 SQL 명령어를 재사용하여 코드의 중복과 무의미한 코드 작성 생략 가능
- 또한 SQL문을 xml에 작성하여 변환이 자유롭고 가독성이 좋게 함

### 사용 방식

1. xml만을 이용한 SQL문 설정, DAO에서는  xml을 찾아서 실행하는 코드로 작성
   - 장점 : SQL문은 별도로 xml로 작성되어 SQL문의 수정이나 유지보수에 적합
   - 단점 : 개발 시 코드 양이 많아지고, 복잡성이 증가
2. Annotation과 인터페이스만을 이용하여 SQL문 설정
   - 장점 : 별도의 DAO 없이도 개발가능, 생산성 크게 증가
   - 단점 : SQL문을 Annotation으로 작성하므로, 수정이 필요한 경우 매번 다시 컴파일해야 함
3. 인터페이스와 xml로 작성된 SQL문 활용
   - 장점 : 간단한 SQL문은 Annotation으로, 복잡한 SQL문은 xml으로 처리하여 유연성이 좋음
   - 단점 : 개발자에 따라 개발 방식 차이로 인해 유지보수가 중요한 프로젝트에서는 부적합


### 특징

#### 장점

1. 간단함 : 배우기 쉬움
2. 생산성 : 62% 줄어드는 코드, 간단한 설정
3. 성능 : 데이터 접근 속도를 높여주는 Join 매핑 ⇒ 구조적 강점
4. 관심사 분리 : 설계를 향상(유지보수성) 리소스를 관리하여 계층화 지원
5. 작업 분배 : 팀을 세분화하는 것을 도움
6. SQL문이 애플리케이션 소스 코드로부터 완전 분리
7. 이식성 : 어떤 프로그래밍 언어로도 구현 가능
8. 오픈소스이며 무료

#### 단점

1. 스키마 변경 시 SQL 쿼리를 직접 수정해줘야 함
2. 반복된 쿼리가 발생하여 반복 작업이 존재
3. 런타임 시에 오류가 확인될 수 있음
4. 쿼리를 직접 작성하기 때문에 데이터베이스에 종속된 쿼리문 발생
5. 데이터베이스 변경 시 로직도 같이 수정해줘야 함

## JPA(Java Persistence API)

- Java ORM 기술에 대한 API 표준 명세

> 💡 **ORM(Object Relation Mapping)**<br>
> - 객체(Object)와 DB 테이블을 매핑시켜 RDB 테이블을 객체지향적으로 사용하게 해주는 기술
> - RDB 테이블은 객체지향적 특성이 없어 Java 같은 객체지향 언어로 접근하기 어려움
> - ORM을 통해 보다 객체지향적으로 RDB 사용 가능
> - 따라서, SQL을 직접 작성하지 않고 표준 인터페이스 기반으로 처리할 수 있음

- JPA는 단순한 명세이기 때문에 JPA만 가지고는 어떤 구현 기술을 사용할 수 없음
- 실제로 사용하는 Repository는 Spring Data JPA가 제공하는 기술
- Spring Data JPA : JPA를 간편하게 사용하도록 만들어 놓은 오픈 소스
- 그리고 JPA의 구현체는 여러 개가 존재 → Hibernate, EclipseLink, DataNucleus 등
- **이 중 Hibernate가 많이 쓰임**, 범용적으로 다양한 기능을 제공해주기 때문

### 특징

#### 장점

1. 1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩을 제공하여 성능상 이점을 얻음
2. 코드 레벨로 관리 ⇒ 사용하기 용이 및 생산성 높음
3. 컴파일 타임에 오류 확인 가능
4. 데이터베이스에 비종속적 ⇒ 특정 쿼리를 사용하지 않아 추상적으로 기술 구현 가능
5. 엔티티로 관리되므로 스키마 변경 시 엔티티만 수정하게 되면 엔티티를 사용하는 관련 쿼리는 자동으로 변경된 내역이 반영
6. 개발 초기에는 쿼리에 대한 이해도가 부족해도 코드 레벨로 어느정도 커버 가능
7. 객체지향적 데이터 관리 가능
8. 부족한 부분은 다양한 쿼리 빌더와 호환하여 보완 가능

#### 단점

1. 복잡한 연산을 수행하기에는 다소 무리가 있음 → 로직이 복잡하거나 불필요한 쿼리 발생
2. 초기에 생산성이 높을 수 있으나, 점차 사용하다보면 성능상 이슈 발생 → N+1 문제, FetchType, Proxy, 연관 관계
3. 고도화될 수록 러닝 커브가 높아질 수 있음 → 성능 이슈의 연장선으로 해결 방안에 따라 복잡한 내부 로직 이해 필요

## MyBatis vs. JPA
[trends.google.co.kr](https://trends.google.co.kr/trends/explore?date=all&q=/m/026dw9m,/m/0fqmvs4)
- 대세는 JPA
- 다음과 같은 장점을 누릴 수 있기 때문

### 엔티티에 맞는 테이블 생성 및 DB 생성 편리

- 설정에 따라 매핑된 객체를 바탕으로 테이블 자동 생성
- 자동 생성되는 이름이 가독성이 떨어져 그대로 사용하긴 부족
- 그래도 모든 DDL 작성하는 것보단 편함

### 객체지향 중심 개발

- 객체를 이용하면 테이블에 매핑되는 클래스를 더욱 객체지향적으로 개발 가능
- Java에 아아주 적합

### 테스트 작성 용이

- 테이블을 자동으로 만들어주므로 테스트 작성하기에 매우 좋음
- Spring은 Repository 테스트를 위한 @DataJpaTest를 제공
- 기본적으로 인메모리 데이터베이스 `h2` 로 연결됨
- 테이블 생성 옵션을 주면 손쉽게 Repository 계층 테스트 가능

### 기본적인 CRUD 자동화

- 테이블과 객체를 매핑시키는 기술이므로 기본적인 CRUD 제공됨
- MyBatis 이용 시 간단한 CRUD 쿼리들도 모두 작성해줘야 하는 것에 반해, JPA를 이용하면 생산성을 높일 수 있음

### 복잡한 쿼리는 QueryDSL 사용해 처리

- MyBatis의 장점 중 하나는 직접 쿼리를 작성 ⇒ 복잡한 쿼리 다루기 유용
- 이에 반해 JPA는 동적 쿼리를 처리하기가 어려움
- 이 경우, `QueryDSL` 오픈소스를 활용하면 문제 해결 가능

```java
@Repository
@RequiredArgsConstructor
public class QuizRepositoryImpl {

    private final JPAQueryFactory query;

    @Override
    public User search(final String email) {
        final QUser qUser = QUser.user;

        final User user = query.selectFrom(qUser)
                .where(qUser.email.equalsIgnoreCase(email))
                .fetch();
        return user;
    }
}
```

# Reference

[[Java] 자바 퍼시스턴스 프레임워크 ( Java Persistence Framework )](https://dev-coco.tistory.com/76)

[[Spring] JPA vs MyBatis 정리 (특징, 장점, 단점 등)](https://sm-studymemo.tistory.com/95)

[[Java] ORM이란? MyBatis와 JPA의 차이, MyBatis보다 JPA를 사용해야 하는 이유](https://mangkyu.tistory.com/20)