# QueryDSL

## 환경설정
- (build.gradle 참고)

## 시작
- (querydsl.QueryDSLEx 패키지 참고)

### 검색 조건 쿼리
```java
query.from(member)                                  
    .where(member.username.contains("member"))
    .fetch().forEach(member -> System.out.println("member = " + member));

// 결과
select
    member1
from
    Member member1
where
    member1.username like ?1 escape '!'
from
    member member0_
where
    member0_.username like ? escape '!'
```
- `where` 절에는 `and`, `or` 사용 가능 -> 여러 검색 조건 사용 시 `and` 연산
    ```java
    // AND 연산
    .where(member.username.contains("member"), member.team.id = 1L)
    ```
- 다른 메서드는 IDE 자동완성을 통해 쉽게 찾을 수 있음

### 결과 조회
>💡 JPAQueryFactory로 수행 시 fetch()로 동작, 우선 책에 있는 내용을 기술 -> 실제로 JPAQuery 클래스에 남아 있는 메서드

- 쿼리 작성이 끝나고 결과 조회 메서드 호출 시 실제 DB를 조회
- 보통 `uniqueResult()`나 list()를 사용하고 파라미터로 프로젝션 대상을 넘김
  - `uniqueResult()`
    - 조회 결과가 한 건일 때 사용
    - 없으면 null 반환
    - 하나 이상이면 `NonUniqueResultException` 발생
  - `singleResult()`
    - `uniqueResult()`와 동일
    - 결과가 하나 이상이면 처음 데이터를 반환
  - `list()`
    - 결과가 하나 이상일 떄 사용
    - 없으면 빈 컬렉션 반환

### 페이징과 정렬

```java
query.from(member)                                  
    .where(member.username.contains("member"))
    .offset(0).limit(20)                // 페이징은 offset(), limit()를 적절히 조합해서 사용
    .orderBy(member.username.desc())    // 정렬은 asc(), desc()
    .fetch().forEach(member -> System.out.println("member = " + member));

// QueryModifier로도 사용 가능
query.from(member)
    .restrict(new QueryModifiers(20L, 10L));    // limit, off   set
```

```java
// 페이징 처리를 하려면 검색된 전체 데이터 수를 알아야 함
// 이때는 fetchResults() 사용
QueryResults<?> result = query.from(member)
        .groupBy(member.team)
        .offset(0).limit(20)
        .having(member.team.id.eq(1L))
        .fetchResults();
long total = result.getTotal();             // 검색된 전체 데이터 수
long limit = result.getLimit();             
long offset = result.getOffset();
List<?> results = result.getResults();      // 조회된 데이터
```

### 그룹
```java
query.from(member)
        .groupBy(member.team)
        .offset(0).limit(20)
        .having(member.team.id.eq(1L))
        .fetch();
```

### 조인
```java
query.from(QOrder.order)
        .join(QOrder.order.member, member)
        .leftJoin(QOrder.order.product, QProduct.product)
        .on(QProduct.product.stockAmount.gt(2))             // on도 사용
        .fetch();
```
- `innerJoin(join)`, `leftJoin`, `rightJoin`, `fullJoin` 사용 가능
- `JPQL`의 `on`과 성능 최적화를 위한 fetch 조인도 가능
  > 💡4 버전 이후로 `fetch()`를 사용하는데, 이를 통해 fetch가 자동으로 수행되는 것으로 보임

```java
// from 절에 여러 조건 사용
query.select(...)
        .from(member, QOrder.order)
        .where(QOrder.order.member.eq(member))
        .fetch();
```

### 서브 쿼리
```java
query.select(QOrder.order,
    // 4버전 이후 서브쿼리 사용 방식 변경
    ExpressionUtils.as(
        JPAExpressions.select(QProduct.product)
            .from(QProduct.product)
            .where(QOrder.order.product.eq(QProduct.product)),
        "product"
    ))
    .from(QOrder.order)
    .fetch();
```

### 프로젝션과 결과 반환

- 프로젝션`Projection`: select 절에 조회 대상을 지정하는 것

#### 프로젝션 대상이 하나
```java
List<Member> result = query.select(member).from(member).fetch();
```

#### 여러 컬럼 반환과 튜플
```java
List<Tuple> result = query.select(member.id, member.username).from(member).fetch();
result.forEach(ret -> {
    System.out.println("id = " + ret.get(0, Long.class));
    System.out.println("username = " + ret.get(1, String.class));
});
```
- 프로젝션 대상으로 여러 필드 선택 시 기본으로 `com.querydsl.core.Tuple` 반환

#### 빈 생성

- 쿼리 결과를 엔티티가 아닌 특정 객체로 받고 싶으면 빈 생성`Bean Population` 기능을 사용
- 다양한 방법 제공

##### 사용할 DTO
```java
// 빈 생성을 위해선 기본 생성자와 setter 필수
// see: https://jhkimmm.tistory.com/30
@NoArgsConstructor
@Setter
public class UserDTO {

    private String username;
    private int age;

    public UserDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}

```


##### 프로퍼티 접근
```java
query.select(Projections.bean(UserDTO.class, member.username, member.age))
        .from(member).fetch()
    .forEach(userDTO -> System.out.println("userDTO = " + userDTO));
```
- DTO의 필드 명과 쿼리 내 컬럼 명이 다르면 `as`를 사용해서 별칭을 주면 됨

##### 필드 직접 접근
```java
query.select(Projections.fields(UserDTO.class, member.username, member.age))
        .from(member).fetch()
    .forEach(userDTO -> System.out.println("userDTO = " + userDTO));
```
- 필드에 직접 접근해서 값을 채워줌
- 필드를 `private`으로 설정해도 정상 동작하며, `setter` 필요 없음 

##### 생성자 사용
```java
query.select(Projections.constructor(UserDTO.class, member.username, member.age))
        .from(member).fetch()
    .forEach(userDTO -> System.out.println("userDTO = " + userDTO));
```
- 생성자를 사용함
- 지정한 프로젝션과 파라미터 순서가 같은 생성자가 필₩

##### DISTINCT
```java
query.selectDistinct(Projections.constructor(UserDTO.class, member.username, member.age))
        .from(member).fetch()
    .forEach(userDTO -> System.out.println("userDTO = " + userDTO));
```

### 수정, 삭제 배치 쿼리
```java
query.update(member).where(member.username.eq("member1"))
      .set(member.username, "Changed")
      .execute();
```
- 실제로 정상 수행되려면 `commit()`, `clear()`가 수행되어야 함

### 동적 쿼리
```java
query.select(member)
        .from(member)
        .where(new BooleanBuilder()
                .and(member.username.contains("member"))
                .and(member.age.gt(20))
        ).fetch()
    .forEach(member -> System.out.println("member = " + member));
```
- `com.query.BooleanBuilder`를 사용해 특정 조건에 따른 동적 쿼리를 편리하게 생성 가능

### 메서드 위임
```java
public class MemberExpression {
  @QueryDelegate(Member.class)
  public static BooleanExpression isOld(QMember member) {
    return member.age.gt(30);
  }
}

query.select(member)
        .from(member)
        .where(member.isOld()).fetch()
    .forEach(member -> System.out.println("member = " + member));
```
- 쿼리 타입에 검색 조건을 직접 정의 가능
- static 메서드를 만들고 `@com.querydsl.core.annotations.QueryDelegate` 어노테이션 지정
- queryDSL을 gradle에서 컴파일 시 아래와 같은 검색 조건 로직이 Q Class에 포함되어 있음
  ```java
  @Generated("com.querydsl.codegen.EntitySerializer")
  public class QMember extends EntityPathBase<Member> {
    ...
    
    public BooleanExpression isOld() {
        return MemberExpression.isOld(this);
    }
  }
  ```
- 필요하다면 `String`, `Date` 같은 Java 기본 내장 타입에도 메서드 위임이 가능