package jpabook.entitymapping;

import javax.persistence.*;
import java.util.Date;

@Entity                     // Entity 속성
// name:
// - JPA에서 사용할 엔티티 이름, 보통 기본값인 클래스 이름을 사용
// - 다른 패키지에 이름이 같은 엔티티 클래스 존재 시 이름 지정해서 충돌하지 않도록 해야 함
//
// Entity 적용 시 주의사항
// 1. 기본 생성자는 필수 -> 보통 protected 기본 생성자 선언
// 2. final 클래스, enum, interface, inner 클래스에 사용 불가
// 3. 저장할 필드에 final 사용 X

// 엔티티와 매핑할 테이블 지정, 생략 시 매핑한 엔티티 이름을 테이블 이름으로 사용
@Table(
    name = "MEMBER",                        // name: 매핑할 테이블 이름
    uniqueConstraints = {                   // uniqueConstraints(DDL):
                                            // - DDL 생성 시 유니크 제약조건을 걺
                                            // - 2개 이상의 유니크 제약조건 만들 수 있음, 해당 기능은 스키마 자동 생성 기능 사용해서 DDL 만들 때만 사용
        @UniqueConstraint(
            name = "NAME_AGE_UNIQUE",
            columnNames = {"NAME", "AGE"}
        )
    }
)
// catalog: catalog 기능 있는 DB에서 catalog 매핑
// schema: schema 기능 있는 DB에서 catalog 매핑
public class Member {

    // 기본 키 매핑 방법
    // 1. 직접 할당:
    //      em.persist() 호출 전에 애플리케이션에서 직접 식별자 값 할당
    //      식별자 값 없이 저장하면 예외 발생명
    //      해당 예외는 JPA 표준에 안정해짐
    //      Hibernate 사용 시 최상위 예외(PersistenceException) 발생하고,
    //      내부에 IdentifierGenerationException(Hibernate) 예외 발생
    // 2. @GeneratedValue: 자동 생성, 대리 키 사용 방식
    //  IDENTITY:
    //      기본 키 생성을 DB에 위임
    //      DB에 엔티티를 저장해서 식별자 값 획득 후 영속성 컨텍스트에 저장
    //  SEQUENCE:
    //      DB 시퀀스를 사용해서 식별자 값 획득 후 영속성 컨텍스트에 저장
    //  TABLE:
    //      키 생성용 테이블 만들고 시퀀스처럼 사용 -> 모든 DB에서 사용 가능
    //
    // 자동 생성 전략 다양성의 이유
    // DB 벤더마다 지원하는 방식이 다름
    //  시퀀스의 경우 Oracle O, MySQL X
    //  대신 MySQL은 AUTO_INCREMENT 제공
    // 따라서 전략은 DB에 의존하므로 전략이 다양함
    //
    // @Id 적용 가능 Java 타입
    // Primitive Type
    // Java Wrapper 클래스
    // String
    // java.util.Date입
    // java.sql.Date
    // java.math.BigDecimal
    // java.math.BigInteger
    //
    // 식별자 선택 전략
    // 기본 키는 3가지 조건 모두 만족해야 함
    //  1. null 값 허용안함
    //  2. 유일해야 함
    //  3. 변해선 안됨
    // 전략은 크게 2가지
    //  1. 자연 키 : 비즈니스에 의미가 있는 키 -> 이메일, 주민번호, 전화번호
    //  2. 대리 키(대체 키): 비즈니스와 관련 없는 임의로 만들어진 키 -> Oracle 시퀀스, auto_increment
    // 권고 전략 => 대리 키
    //  비즈니스 규칙은 생각보다 쉽게 변함
    //  ex. 주민번호(자연 키)를 기본 키로 한 경우
    //     기본 키의 조건 모두 만족함
    //     하지만 정부 정책으로 인해 주민 번호를 저장할 수 없음 => 기본 키를 제외하기 위해 많은 작업이 발생
    //     평생 기본 키 조건을 모두 만족하는 데이터는 찾기 어려움 => 대리 키를 통해 조건 만족과 외부 요인으로 인한 변경을 막음
    @Id

    //  IDENTITY: 기본 키 생성을 DB에 위임
    //      MySQL, PostgreSQL, SQL Server, DB2에 사용
    //      DB에 값을 저장하고 나서야 기본 키 값을 구할 수 있을 때 사용
    //      해당 전략은 DB에 데이터 INSERT 후에 기본 키 값 조회 가능 -> 식별자 할당을 위해 DB 조회 필수
    //      Hibernate는 JDBC3의 Statement.getGenreatedKeys()를 통해 DB 한번만 통신해 최적화함
    //          *getGenratedKeys(): 데이터 저장과 동시에 생성된 기본 키값도 가져올 수 있음
    //      주의점
    //          엔티티 영속 상태되려면 식별자 필수
    //          IDENTITY는 엔티티를 DB에 저장해야 식별자를 구할 수 있음 -> em.persist() 즉시 INSERT SQL 전달 -> 쓰기 지연(Write-behind) 동작하지 않음
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false, length = 10)   // DDL에 제약 조건 추가 가능
    private String username;

    private Integer age;

    // 다양한 매핑 사용인
    @Enumerated(EnumType.STRING)            // enum 매핑
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)       // Java의 날짜 타입은 @Temporal 사용해서 매핑
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob                                    // DB의 VARCHAR 타입 대신 CLOB 타입으로 저장
    private String description;

    protected Member() {
    }

    public Member(
            String username,
            Integer age,
            RoleType roleType,
            Date createDate,
            Date lastModifiedDate,
            String description
    ) {
        this.username = username;
        this.age = age;
        this.roleType = roleType;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
