package jpabook.entitymapping;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
/**
 * @Access: 엔티티 데이터에 접근하는 방식 지정
 */
@Access(
    AccessType.FIELD            // 필드에 직접 접근, private이어도 접근 가능
    /*AccessType.PROPERTY*/     // 접근자(Getter)를 사용
)
public class FieldAndColumnMapping {

    /**
     * @Column: 객체 필드를 테이블 컬럼에 매핑
     */
    @Column(
        // name: 필드와 매핑할 테이블 컬럼 이름
        // insertable: 엔티티 저장 시 같이 저장(default: true, false 시 읽기 전용)
        // updatable: 엔티티 수정 시 같이 수정(default: true, false 시 읽기 전용)
        // table: 하나의 엔티티를 두 개 이상의 테이블에 매핑 시 사용, 지정한 필드를 다른 테이블에 매핑 가능(default: 현 클래스가 매핑된 테이블)
        // nullable(DDL): null 값 허용 여부, false 설정 시 DDL 생성 시 not null 제약 조건이 붙음
        // unique(DDL): 한 컬럼에 유니크 제약 조건, 두 개이상 제약조건 시 클래스 레벨에서 uniqueConstraints 사용
        // columnDefinition(DDL): DB 컬럼 정보를 직접 줄 수 있음(default: 필드의 Java 타입과 방언 정보를 활용해 적절한 컬럼 타입 생성)
        // length(DDL): 문자 길이 제약 조건, String 타입에만 사용(default: 255)

        // BigDecimal/BigInteger에서 사용, double, float 안 먹힘, 아주 큰 숫자나 정밀한 소수를 다뤄야할 때만 사용
        // precision(DDL): 소수점 포함 전체 자릿수
        // scale(DDL): 소수점 자릿수
    )
    String value1;

    /**
     * @Column 생략
     */
    int value2;         // Java 기본 타입 -> NOT NULL
    Integer value3;     // Java 객체 타입 -> NULLABLE

    @Column
    int value4;         // @Column 선언(nullable = true) -> NULLABLE

    /**
     * @Enumerated: Java의 enum 타입 매핑할 때 사용
     */
    @Enumerated(
        /*value = EnumType.ORDINAL */       // enum 순서를 DB에 저장
                                            //  장점: DB 저장되는 데이터 크기 작음
                                            //  단점: 이미 저장된 enum 순서 변경 불가
        value = EnumType.STRING             // enum 이름을 DB에 저장
                                            //  장점: 저장된 enum 순서가 바뀌거나, enum이 추가되도 안전
                                            //  단점: DB 저장되는 데이터 크기가 ORDINAL 보다 큼
    )
    RoleType roleType;

    /**
     * @Temporal: 날짜 타입(java.util.Date, java.util.Calendar) 매핑 시 사용
     * 생략 시 Date 클래스와 가장 유사한 timestamp로 정의됨
     * 일부 DB는 datetime을 예약어로 사용하나, 방언을 통해 코드 수정할 필요가 없음
     */
    @Temporal(value = TemporalType.DATE)        // 날짜, DB date 타입과 매핑(2023-10-16)
    Date date;
    @Temporal(value = TemporalType.TIME)        // 시간, DB time 타입과 매핑(11:11:11)
    Date time;
    @Temporal(value = TemporalType.TIMESTAMP)   // 날자와 시간, DB timestamp 타입과 매핑(2023-10-16 11:11:11)
    Date timestamp;

    @Lob                                        // CLOB: String, char[], java.sql.CLOB
                                                // BLOB: byte[], java.sql.BLOB
    String value5;

    @Transient                                  // 매핑하지 않음 -> 객체에 임시로 값을 보관하고 싶을 때 사용
    String value6;


    @Id                                         // 필드에 @Id가 있으므로 FIELD 설정한 것과 같음 -> @Access 생략 가능
    private Long id;

    @Id                                         // 프로퍼티에 @Id가 있으므로 PROPERTY 설정과 같음 -> @Access 생략 가능
    public Long getId() {
        return id;
    }

    /**
     * 아래처럼 필드 접근과 프로퍼티 접근 혼용 가능
     */
    @Id
    private String strId;

    @Transient
    private String firstName;
    @Transient
    private String lastName;

    /**
     * FULLNAME 컬럼에 firstName + lastName 결과가 저장됨
     */
    @Access(AccessType.PROPERTY)
    public String getFullName() {
        return firstName + lastName;
    }
}
