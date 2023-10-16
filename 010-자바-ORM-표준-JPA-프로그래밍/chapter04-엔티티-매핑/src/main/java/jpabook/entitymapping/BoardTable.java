package jpabook.entitymapping;

import javax.persistence.*;

@Entity
// TABLE:
// 키 생성 전용 테이블 생성
// 여기에 이름과 값으로 사용할 컬럼을 만들어 DB 시퀀스를 '흉내'내는 전략 -> 모든 DB에 적용 가능
//
// DDL 결과:
// create sequence BOARD_SEQ start with 1 increment by 1
// create table MY_SEQUENCES (
//      sequence_name varchar(255) not null ,       // 시퀀스 이름
//      next_val bigint,                            // 시퀀스 값,
//      primary key ( sequence_name )
// )
// MY_SEQUENCES 테이블 내부
// sequence_name    next_val
// BOARD_SEQ        2
// MEMBER_SEQ       10
// PRODUCT_SEQ      2
// ...
//
// 테이블을 생성해서 관리하는 것만 제외하면 SEQUENCE 전략과 내부 동작방식이 동일
@TableGenerator(
    name = "BOARD_SEQ_GENERATOR",       // 식별자 생성기 이름, @GenearatedValue.generator와 일치
    table = "MY_SEQUENCES",             // 키 생성 테이블 이름
    // valueColumnName                  시퀀스 값 컬럼명(default: next_val)
    pkColumnValue = "BOARD_SEQ",        // 키로 사용할 이름(default: 엔티티 이름)
    allocationSize = 1                  // 시퀀스 한번 호출 후 증가하는 수
    // initialValue                     초기 값
    // catalog, schema                  DB catalog, schema 이름
    // uniquConstraints(DDL)            유니크 제약 조건

    // Hibernate 기준 테이블 매핑(JPA 표준에는 table, pkColumnName, valueColumnName 기본값을 구현체가 정함)
    // {pkColumnName}   {valueColumnName}
    // {ppkColumnValue} {initialValue}

    // TABLE 전략도 값을 조회를 위해 SELECT, 다음 값 증가를 위해 UPDATE를 호출
    // SEQUENCE와 비슷하게 DB 접근을 한번 더 하는 단점이 있음 => allocationSize를 통해 최적화 가능
)
public class BoardTable {

    @Id
    @GeneratedValue(
        strategy = GenerationType.TABLE,
        generator = "BOARD_SEQ_GENERATOR"
    )
    private Long id;

    public Long getId() {
        return id;
    }
}
