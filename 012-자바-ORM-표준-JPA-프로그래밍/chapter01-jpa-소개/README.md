# JPA 소개

## ORM(Object-Relational Mapping) Framework
- 객체와 관계형 데이터베이스를 매핑해 패러다임의 불일치 문제를 해결하는 프레임워크
- 객체 모델과 관계형 데이터베이스 모델은 지향하는 패러다임이 서로 달라, 이 차이를 극복하기 위한 많은 시간과 코드를 소비
  - *패러다임: 견해나 사고를 근본적으로 규정하고 있는 인식의 체계, 각 모델의 기준
    ```java
    // 자바에는 동일성 비교(==)과 동등성 비교(equals)가 존재
    public class Member {
      String id;
    
      @Override
      public boolean equals(Object o) {
        // 동등성 비교에 대한 추가 처리 필요
      }
    }
  
    public class MemberDao {
    
      public static Member get(String id) {
        // DB 조회 후 멤버 반환
      }
    }
  
    // m1 == m2 : false
    // 같은 객체로 처리되야 하는데 다른 객체로 인식
    // 하지만 DB 기준으로 보면 id가 같으므로 같은 객체로 인식되어야 함
    Member m1 = MemberDao.get("asdf");
    Member m2 = MemberDao.get("asdf");
    ```
- 이러한 객체 모델링과 관계형 데이터베이스 사이의 차이점을 해결
- **JPA**가 바로 자바 진영의 ORM 기술 표준

## SQL을 직접 다룰 때 발생하는 문제점

### JDBC API
![img.png](img.png)
- Java에서 DB를 다루기 위한 API
- 해당 API를 통해 SQL 쿼리를 DB에 보내 데이터를 가져옴

### 번거로운 반복
```java
public class member {
    private String memberId;
    private String name;
    ...
}

public class MemberDao { 
    public Member find(String memberId) {
        // 1. 조회용 쿼리
        String query = "SELECT MEMBER_ID, NAME FROM MEMBER M WHERE MEMBER_ID = ?";
        // 2. DB연결 객체에서 쿼리 실행을 위한 객체를 가져와 SQL 실행 
        ResultSet rs = stmt.executeQuery(sql);
        
        // 3. 조회 결과를 Member로 매핑
        String memberId = rs.getString("MEMBER_ID");
        String name = rs.getString("NAME");
        Member member = new Member();
        member.setMemberId(memberId);
        member.setName(name);
    }
    
    public void save(Member member) {
        // 1. 등록용 쿼리
        String query = "INSERT INTO MEMBER(MEMBER_ID, NAME) VALUES(?, ?)";
        // 2. DB연결 객체에서 쿼리 실행을 위한 객체를 가져와 INSERT SQL에 전달
        pstmt.setString(1, member.getId());
        pstmt.setString(2, member.getName());
        // 3. JDBC API 사용해서 SQL 실행
        pstmt.executeUpdate(query);
    }
}
```
- 해당 작업이 RDB가 없다면 아래와 같은 로직
```java
List<Member> members = new ArrayList<>(); // 데이터 관리 컨테이너
...
members.add(member1);   // save
members.add(member2);   // save
members.get(0);         // find
```
- RDB는 객체 구조와는 다른 '데이터 중심'의 구조를 가지므로 객체를 RDB에 직접 저장하거나 조회할 수 없음
- 따라서 직접 SQL과 JDBC API를 사용해서 객체와 DB 중간에서 변환 작업이 필요 => 지나친 번거로움

### SQL 의존적 개발
#### Case 1 - 회원 연락처 추가
- 추가
    ```java
    public class member {
        private String memberId;
        private String name;
        private String tel;
        ...
    }
    ```
- 조회 
    ```java
    "SELECT MEMBER_ID, NAME, TEL FROM MEBER WEHRE MEMBER_ID = ?";
    ``` 
  - 연락처도 조회할 수 있도록 수정
- 수정
  - 연락처 수정을 위해 Dao에 수정 로직에도 처리해야 함
  - 만약 회원 객체를 DB가 아닌 Java 컬렉션에 보관하면 아래처럼 단순
      ```java
      list.add(member);       // 등록
      member = list.get(???); // 조회
      member.setTel("010-");  // 수정
      ```
#### Case 2 - 연관된 객체
- 회원은 한 팀에 필수로 소속해야하는 요구사항 추가
    ```java
    public class member {
        private String memberId;
        private String name;
        private String tel;
        private Team team;      // 추가
        ...
    }
    
    class Team {
        private String teamName;
        ...
    }
    
    public class MemberDao {
        ...
        public Member findWithTeam(String memberId) {
            // JOIN문으로 Team까지 처리
            "SELECT M.MEMBER_ID, M.NAME, M.TEL, T.TEAM_NAME" 
            + "FROM MEMBER M"
            + "JOIN TEAM T" 
            + "ON M.TEAM_ID = T.TEAM_ID";
        } 
    }
    ```
- 여기서 Member가 연관된 Team 사용 가능 여부는 사용하는 SQL에 달려 있음을 알 수 있음
- 이 방식의 가장 큰 문제점은 데이터 접근 계층을 사용해 SQL을 숨겨도 DAO를 열어서 어떤 SQL이 실행되는지 확인해야 함
- SQL에 모든 것을 의존하는 상황에선 개발자들이 `엔티티`를 신뢰하고 사용할 수 없음
  - *엔티티: Member나 Team처럼 비즈니스 요구사항을 모델링한 객체
- 진정한 의미의 계층 분할이 아님 -> 물리적으론 분리했으나 논리적으론 엔티티와 강한 의존관계를 가짐
  - 이 때문에 필드가 하나 변경되어도 이에 따라 CRUD 코드와 SQL의 대부분을 수정해야 함

### 객체지향에서 SQL 사용으로 인한 문제점 정리 
- 진정한 의미의 계층 분할이 어려움
- 엔티티를 신뢰할 수 없음
- SQL에 의존적인 개발을 피하기 어려움

## JPA와 문제 해결
- JPA를 사용하면 객체를 DB에 저장 및 관리 할 때 SQL이 아닌 JPA가 제공하는 API를 사용하면 됨
  ```java
  // 1. 저장
  jpa.persist(member);
  // 2. 조회
  Member member = jpa.find(Member.class, "memberId");
  // 3. 수정
  Member member = jpa.find(Member.class, "memberId");
  member.setName("이름 변경");
  // 4. 연관 객체 조회
  Member member = jpa.find(Member.class, "memberId");
  Team team = member.getTeam();
  ```
  
## 패러다임의 불일치
- 애플리케이션은 발전하면서 내부 복잡성도 커짐
- 객체지향 프로그래밍은 추상화, 캡슐화, 상속, 다형성으로 시스템의 복잡성을 제어하는 장치 제공
- 그래서 비즈니스 요구사항을 정의한 도메인 모델도 객체로 모델링 시 객체지향 언어가 가진 장점 활용
- 문제는 이 도메일 모델을 저장할 때 발생
  - 회원가입 시 회원이라는 객체 인스턴스를 어딘가 영구 보관
  - 기능은 클래스에 정의되어 있으므로, 속성만 DB에 저장했다 필요할 때 불러와 복구하면 됨
  - 하지만 부모 객체를 상속받거나 다른 객체 참조 시 상태(속성)을 저장하기 쉽지 않음
    - ex. 멤버 저장 시 참조된 팀도 같이 저장, 회원만 저장하면 팀에 대한 정보가 사라짐
- Java는 이를 고려해 직렬/역직렬화 지원하나, 직렬화된 객체 검색이 어려우므로 현실성이 없음
- RDB에 객체를 저장하는 것이 방법이나 이것도 문제가 있음
  - RDB는 데이터 중심 구조화되어 있고, 집합적인 사고 요구 -> 객체지향의 추상화, 상속, 다형성 같은 개념 없음
  - 상호 지향하는 목적이 달라 표현 방법이 다름
  - Java라는 객체지향 언어로 개발하고 데이터를 RDB에 저장해야 하면, 이러한 **패러다임 불일치** 문제를 중간에 해결해야 함
    - **이를 해결하는데 많은 시간과 코드가 소비됨**
- **패러다임 불일치로 인해 발생하는 문제를 JPA를 통해 해결 가능**

### 상속
![img_1.png](img_1.png)
- 객체는 상속이 있으나, 테이블은 없음
  - 일부 DB는 지원하나, 객체의 상속과는 약간 다름
  
![img_2.png](img_2.png)
- DB 모델링에서 언급되는 `슈퍼타입`, `서브타입` 관계를 사용 시 상속과 가장 유사한 형태 설계 가능
- 이를 기반한 객체 코드는 아래와 같음
    ```java
    abstract class Item {
        Long id;
        String name;
        int price;
    }
  
    class Album extends Item {
        String artist;
    }
  
    class Movie extends Item {
        String director;
        String actor;
    }
  
    class Book extends Item {
        String author;
        String isbn;
    }
    ```
- 모든 객체는 저장되려면 객체를 분해해서 두 SQL을 만들어야 함
    ```sql
    /* 부모 객체를 먼저 넣고, 자식 객체를 넣는 방식으로 추가해야 함 */
    INSERT INTO ITEM ...
    INSERT INTO ALBUM ...
  
    INSERT INTO ITEM ...
    INSERT INTO MOVIE ...
    ```
- 조회도 마찬가지로 부모 테이블과 자식 테이블 JOIN 후, 그 결과로 객체를 생성해야 함
  - 이런 과정 모두 패러다임 불일치 해결을 위해 소모하는 비용

#### JPA와 상속
- JPA는 상속과 관련된 패러다임 불일치를 해결해줌
    ```java
    jpa.persist(album);   // INSERT INTO ITEM & INSERT INTO ALBUM
    
    Album album = jpa.find(Album.class, "id");  // SELECT I.*, A.*
                                                // FROM ITEM I
                                                // WHERE I.ID = A.ID
    ```
  
### 연관관계
- 객체는 참조를 통해 다른 객체와 연관관계를 가지고 참조에 접근해서 연관객체를 조회
  ```java
  class Movie {
    Actor actor;    // Movie가 Actor 객체 참조
    ...
  }
  
  Actor actor = movie.getActor();   // 참조에 접근해서 연관객체 조회
  ```
- 테이블은 Foreign Key를 사용해서 다른 테이블과 연관관계를 갖고, JOIN을 통해 연관 테이블을 조회
  ```sql
  SELECT M.*, A.*
  FROM MOVIE M 
  JOIN ACTOR A ON M.MOVIE_ID = A.ACTOR_ID
  ```
- 이러한 패러다임 불일치는 극복하기 어려움

#### 객체를 테이블에 맞춰 모델링
```java
class Member {
    String id;
    Long teamId;        // TEAM_ID FK 컬럼 사용
    String username;
}

class Team {
    Long id;            // TEAM_ID PK 컬럼 사용
    String name;
}
```
- 이 경우 객체를 테이블에 저장하거나 조회할 때는 편리함
- 하지만 연관관계 있는 객체를 찾을 땐 어려움
  ```java
  // 일반적으로 member엔 연관된 객체의 참조를 보관해야 아래처럼 사용 가능 -> 가장 객체지향적 방법
  Team team = member.getTeam();
  ```
  - 객체를 테이블에 맞춰 모델링하면 연관된 Team 객체를 참조를 통해 조회하기 불가능

#### 객체지향 모델링
```java
class Member {
  String id;
  Team teamId;        // 참조로 연관관계 맺음
  String username;
}

class Team {
  Long id;            // TEAM_ID PK 컬럼 사용
  String name;
}
```
- 이처럼 사용하면 객체를 테이블에 저장하거나 조회하기 어려움
- MEMBER 테이블은 TEAM_ID를 외래 키로 연관관계를 맺기 때문에 중간에 변환 작업이 필요
  - 저장
    ```java
    // 저장(Java)
    member.getId();           // MEMBER_ID 저장
    member.getTeam().getId(); // TEAM_ID FK 저장
    ...
    ```
  - 조회
    ```sql
    // 조회(sql)
    SELECT M.*, T.*
      FROM MEMBER M  
      JOIN TEAM T ON M.MEMBER_ID = TEAM_ID
    ```
    ```java
    // SQL 실행

    // DB 조회한 회원 정보 모두 입력
    Member member = new Member();
    ...
    
    // DB 조회한 팀 정보 모두 입력
    Team team = new Team();
    ...
    
    // 관계 설정
    member.setTeam(team);
    return member;
    ```
- 이런 과정 모두 패러다임 불일치 해결을 위해 소모하는 비용

#### JPA와 연관관계
- JPA는 이러한 패러다임 불일치를 해결함 
  ```java
  // 저장
  member.setTeam(team); // 회원과 팀 연관관계 설정
  jpa.persist(member);  // 회원과 연관관계 함께 저장
  
  // 조회
  Member meber = jpa.find(Member.class, memberId);  // 외래 키를 참조로 변환도 가능
  Team team = member.getTeam();
  ```
  
### 객체 그래프 탐색
- 객체에서 참조를 사용해서 연관된 객체를 찾는 것을 **객체 그래프 탐색**이라 함

![img_3.png](img_3.png)
- 위처럼 설계되었있다고 가정하면 멤버의 주문 아이템을 조회하는 코드는 아래와 같음
  ```java
  member.getOrder().getOrderItem();
  ```
- 객체는 자유롭게 그래프 탐색이 가능해야 함
- DAO에서 아래처럼 SQL을 날려 객체 생성 시 다른 객체 그래프 탐색은 불가능
    ```sql
    /* DAO에서 수행한 객체 조회 쿼리 */
    SEELCT M.*, T.*
    FROM MEMBER M
    JOIN TEAM T ON M.MEMBER_ID = T.TEAM_ID
    ``` 
    ```java
    member.getTeam();   // OK
    member.getOrder();  // X, null 반환
    ```
  - SQL을 직접 다루면 실행한 SQL에 따라 객체 그래프를 어디까지 탐색할 수 있는지가 정해짐
  - 이는 큰 제약, 비즈니스 로직에 따라 사용하는 객체 그래프가 다른데 언제 끊어질지 모를 객체 그래프를 탐색하는 것은 위험
    - 이를 알아보려면 DAO 로직에 있는 SQL문을 직접 분석하는 수 밖에 없음
    - 결국 MemberDAO의 회원 조회 메서드를 상황에 따라 여러개 만들어야 함
        ```java
        // 이것도 결국 코드량이 많아 복잡해짐
        dao.getMember();
        dao.getMemberWithTeam();
        dao.getMemberWithTeamWithOrder();
        ```
#### JPA와 객체 그래프 탐색
- JPA는 객체 그래프 탐색이 자유로움
- 또한 JPA는 적절한 SELECT SQL을 실행함
  - 지연 로딩: 실제 객체를 사용하는 시점까지 DB 조회를 지연
```java
Member member = jpa.find(Member.class, "memberId"); // 처음 조회 시점에 SELECT MEMBER

Order order = member.getOrder();
order.getOrderDate();                               // Order를 사용하는 시점에 SELECT ORDER
```
- 물론 Member와 Order를 즉시 함께 조회하겠다고 설정하면 JPA에서 연관된 Order도 함께 조회함

## JPA

![img_4.png](img_4.png)
- JPA는 자바 진영의 ORM 기술 표준
- 위처럼 애플리케이션(DAO)과 JDBC 사이에서 동작
- 단순히 SQL을 대신 DB에 전달해주는 것뿐만 아니라 다양한 패러다임 불일치 문제도 해결해줌
- 이를 통해 데이터 중심인 RDB를 사용해도 객체지향 개발에 집중 가능
- Java 진영엔 다양한 ORM 프레임워크가 있고, 그 중에 하이버네이트(hibernate) 프레임워크가 많이 사용됨 -> 대부분의 패러다임 불일치 문제 해결
  > **JPA vs. Hibernate**
  > - JPA: ORM 기술에 대한 API '표준(Standard)' 명세
  > - Hibernate: JPA라는 표준의 구현체 프레임워크
  >    
  >   ![img_5.png](img_5.png)
  >   - 언급된 구현체 중 Hibernate가 대중적

> **JPA 변천사**
> - 과거 Enterprise Java Beans(EJB)라는 기술 표준이 있었음
> - 그리고 엔티티 빈이라는 ORM 기술도 있었으나, 복잡하고 기술 성숙도도 떨어졌고 Java Enterprise(J2EE) 애플리케이션 서버에서만 동작
> - 이때 Hiberanate라는 오픈소스 ORM 프레임워크가 등장
>   - 훨씬 가벼우면서 실용적이고, 기술 성숙도도 높았음
>   - 무엇보다 J2EE 없이도 동작
> - 결국 EJB 3.0에서 Hibernate 기반으로 새로운 Java ORM 기술 표준이 나왔는데 이것이 **JPA**
> 
> **JPA 버전별 특징**
> - JPA 1.0(JSR 220) '06 : ch기 버전, 복합 키와 연관관계 기능 부족
> - JPA 2.0(JSR 317) '09 : 대부분의 ORM 기능 포함, JPA Criteria 추가
>   - *Criteria: JPQL 작성을 도와주는 빌더 클래스
> - JPA 3.0(JSR 338) '13 : Stored Procedure 접근, Converter, Entity Graph 기능 추가

### 왜 JPA를 사용해야 하는가?
#### 생산성
- Java 컬랙션에 저장하듯이 JPA를 통해 RDB에 객체 저장 가능
- 지루하고 반복저긴 코드와 CRUD용 SQL을 직접 작성할 필요가 없음
- 추가로 CREATE TABLE 같은 DDL 문을 자동으로 생성해주는 기능도 존재
- 이를 통해 테이블 설계 중심 패러다임을 객체 설계 중심으로 역전

#### 유지보수
- 엔티티에 필드가 변경되면 등록, 수정, 조회를 위한 모든 JDBC API 사용 코드를 수정해야 함
- JPA는 이런 과정을 대신 처리함 -> 유지보수해야할 코드가 줄어듦

#### 패러다임 불일치 해결
- 상속, 연관관계, 객체 그래프 탐색, 비교와 같은 패러다임 불일치 문제를 해결

#### 성능
- 애플리케이션과 DB간 다양한 성능 최적화 기회를 제공
  ```java
  Member member1 = jpa.find("hello");   // 처음 SELECT를 통해 가져옴
  Member member2 = jpa.find("hello");   // 두번째 조회한 건 이미 조회한 것을 재사용
  ```
- 참고로 Hibernate는 SQL 힌트를 넣을 수 있는 기능도 제공
  - *힌트: 오라클 옵티마이저에게 SQL 실행을 위한 데이터 스캐닝 경로, 조인 방법 등을 알려주기 위해 SQL 구문에 작성하는 지시 구문 -> 직접 최적의 실행 결로를 작성

#### 데이터 접근 추상화 및 벤더 독립성
- RDB는 같은 기능도 벤더마다 사용법이 다른 경우가 많음
- JPA는 애플리케이션과 DB 사이에 추상화된 데이터 접근 계층을 제공해, 특저d DB 기술에 종속되지 않도록 함
  - 다른 DB를 쓸려면 이를 JPA에게 알리면 됨

#### 표준
- JPA는 Jav 진영의 ORM 기술 '표준' -> 다른 구현 기술로 손쉽게 변경 가능