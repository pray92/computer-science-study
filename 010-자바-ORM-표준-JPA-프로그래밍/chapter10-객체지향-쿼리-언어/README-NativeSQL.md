# Native SQL

- `JPQL`은 표준 SQL 지원하는 대부분 문법과 SQL 함수 지원
- 그러나 특정 DB에 종속적 기능은 미지원
  1. 특정 DB만 지원하는 함수, 문법, SQL 쿼리 힌트
  2. 인라인 뷰(From 절에서 사용하는 서브 쿼리), `UNION`, `INTERSECT`
  3. `Stored Procedure`
- 하지만 때로는 해당 기능 필요
- JPA는 특정 DB에 종속적인 기능을 사용할 수 있는 다양한 방법을 열어둠 -> 구현체는 더 다양함
- 지원 방법
  1. 특정 DB만 사용하는 함수
     - `JPQL`에서 Native SQL 함수를 호출(JPA 2.1)
     - 하이버네이트는 DB 방언에 각 DB에 종속적 함수들을 정의해 두거나, 직접 정의
  2. 특정 DB만 지원하는 SQL 쿼리 힌트
     - 하이버네이트를 포함한 몇몇 JPA 구현체들이 지원
  3. 인라인 뷰, `UNION`, `INTERSECT`
     - 하이버네이트는 지원하지 않으나 일부 JPA 구현체들이 지원
  4. Stored Procedure
     - `JPQL`에서 호출 가능(JPA 2.1)
  5. 특정 DB만 지원하는 문법
     - 특정 DB에 너무 종속적인 SQL 문법은 지원하지 않음(ex. 오라클의 CONNECT BY) => `Native SQL` 사용
- `Native SQL` vs. JDBC API
  - `Native SQL`의 경우 엔티티 조회 가능하고, 영속성 컥텍스트의 기능을 그대로 사용 가능
  - JDBC API는 단순히 데이터의 나열만 조회

## Native SQL 사용
```java
// 결과 타입 정의
public Query createNativeQuery(String sqlString, Class resultClass);

// 결과 타입을 정의할 수 없을 때
public Query createNativeQuery(String, sqlString);

// 결과 매핑 사용
public Query createNativeQuery(String, sqlString, String resultSetMapping);
```

### 엔티티 조회
- (nativesql.NativeEx ~결과 매핑 참고)

```java
// Native SQL
em.createNativeQuery(
        "SELECT o.id AS order_id, " +
            "o.quantity AS order_quantity, " +
            "o.item AS order_item, " +
            "i.name AS item_name, " +
        "FROM Order o, Product i " +
        "WHERE (order_quantity > 25) AND (order_item = i.id)",
        "OrderResults");

// Anntation
@SqlResultSetMapping(name = "OrderResults",
        entities = {
                @EntityResult(entityClass = Order.class, fields = {
                        @FieldResult(name = "id", column = "order_id"),
                        @FieldResult(name = "quantity", column = "order_quantity"),
                        @FieldResult(name = "item", column = "order_item")
                })
        },
        columns = {@ColumnResult(name = "item_name")}
)
```
- `@FieldResult`를 사용해서 컬럼 명과 필드 명을 직접 매핑
  - 해당 설정은 엔티티 필드에 정의한 `@Column`보다 조금 앞섬
  - `@FieldResult`를 한 번이라도 사용하면 전체 필드를 `@FieldResult`로 매핑해야 함
    ```sql
    /* A, B 둘다 ID라는 필드를 가짐으로 컬럼 명 충돌 */
    SELECT A.ID, B.ID FROM A, B
    
    /* 다음처럼 별 칭을 적절히 사용하고 @FieldResult로 매핑해야 함 */
    SELECT
        A.ID AS A_ID,
        B.ID AS B_ID
    FROM A, B
    ```
    
#### 결과 매핑 어노테이션 표

##### @SqlResultMapping
| 속성       | 기능                               |
|----------|----------------------------------|
| name     | 결과 매핑 이름                         |
| entities | `@EntityResult`를 사용해 엔티티를 결과로 매핑 |
| columns  | `@ColumnResult`를 사용해 컬럼을 결과로 매핑    |


##### @EntityResult
| 속성                  | 기능                                |
|---------------------|-----------------------------------|
| entityClass         | 결과로 사용할 엔티티 클래스 지정                |
| fields              | `@FieldResult`를 사용해 결과 컬럼을 필드와 매핑 |
| discriminatorColumn | 엔티티의 인스턴스 타입을 구분하는 필드(상속에서 사용)    |

##### @FieldResult
| 속성     | 기능          |
|--------|-------------|
| name   | 결과를 받을 필드 명 |
| column | 결과 컬럼명      |

##### @ColumnResult
| 속성   | 기능      |
|------|---------|
| name | 결과 컬럼 명 |

## Named Native SQL
- (Member Entity, nativesql.NativeEx::queryUsingNamedNativeQuery 참고)

### Native SQL XML 정의
- (ormMember.xml 참고)

### 기타
```java
String sql = "...";
Query query = em.createNativeQuery(sql, Member.class)
        .setFirstResult(10)
        .setMaxResult(20);
```
- DB 방언에 따라 결과는 다르나, Native SQL을 사용해도 페이징 처리 API를 적용할 수 있음
- 또한 `Native SQL`은 관리가 쉽지 않고, 자주 사용 시 특정 DB에 종속적인 쿼리 증가 -> 이식성 저하
  - 될 수 있으면 표준 `JPQL`을 사용
  - 기능이 부족하면 차선택으로 JPA 구현체가 제공하는 기능을 사용
  - 그럼에도 부족함을 느끼면 `MyBatis`나 `JdbcTemplate` 같은 SQL 매퍼와 JPA를 함께 사용


### Stored Procedure(JPA 2.1)
- JPA 2.1부터 지원

#### Stored Procedure 사용
```sql
/* Stored Procedure */
/* 첫 번째 파라미터로 값을 입력, 두 번째 파라미터로 결과 반환 */
DELIMETER //
    
CREATE PROCEDURE proc_multiply (INOUT inParam INT, INOUT outParam INT)
BEGIN
    SET outParam = inParam * 2
END //
```
```java
StoredProcedureQuery spq = em.createStoredProcedureQuery("proc_multiply");
spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);

spq.setParameter(1, 100);
spq.execute();

Integer out = (Integer)spq.getOutputParameterValue(2);
System.out.println("out = " + out);     // 결과: 200
```

#### 파라미터에 이름 사용
```java
StoredProcedureQuery spq = em.createStoredProcedureQuery("proc_multiply");
spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);

spq.setParameter("inParam", 100);
spq.execute();

Integer out = (Integer)spq.getOutputParameterValue(2);
System.out.println("out = " + out);     // 결과: 200
```

#### Named Stored Procedure 사용
```java
// 둘 이상 정의 시 @NamedStoredProcedureQuerys 
@NamedStoredProcedureQuery(
        name = "multiply",
        procedureName = "proc_multiply",
        parameters = {
                @StoredProcedureParameter(name = "inParam", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "inParam", mode = ParameterMode.OUT, type = Integer.class)
        }
)
@Entity
public class Member { ... }

// 사용 예제
StoredProcedureQuery spq = em.createStoredProcedureQuery("multiply");

spq.setParameter("inParam", 100);
spq.execute();

Integer out = (Integer)spq.getOutputParameterValue("outParam");
System.out.println("out = " + out);     // 결과: 200
```