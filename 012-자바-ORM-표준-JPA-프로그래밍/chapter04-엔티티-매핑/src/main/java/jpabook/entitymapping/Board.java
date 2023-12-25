package jpabook.entitymapping;

import javax.persistence.*;

@Entity
// SEQUENCE:
// 시퀀스는 유일한 값을 순서대로 생성하는 특별한 DB 오브젝트
// 해당 시퀀스를 사용해서 기본 키를 생성
//  Oracle, PostgreSQL, DB2, h2 DB에서 사용
@SequenceGenerator(
    name = "BOARD_SEQ_GENERATOR",           // 식별자 생성기 이름, @GeneratedValue.generator 매핑 필요
    sequenceName = "BOARD_SEQ",             // DB에 등록되어 있는 시퀀스 이름(default: hibernate_sequence)
    initialValue = 1,                       // DDL 생성 시에만 사용, 시퀀스 DDL 생성 시 처음 시작 수 지정
    allocationSize = 1                      // 시퀀스 한번 호출에 증가하는 수(Default: 50)
    // catalog, schema: DB 내 catalog, schema 이름

    // format:
    //  create sequence [sequenceName]
    //  start with [initialValue] increment by [allocationSize]

    // JPA 표준 명세에서는 sequenceName의 기본값을 JPA 구현체가 정의하도록 함 -> 위에건 Hibernate 기준

    // allocationSize의 default 50의 이유
    // 최적화 문제, 해당 전략은 DB 시퀀스를 통해 식별자 조회하는 추가 작업 필요 -> DB와 2번 통신
    //  1. 식별자 구하기 위해 DB 시퀀스 조회 -> SELECT BOARD_SEQ.NEXTVAL FROM DUAL
    //  2. 조회한 시퀀스를 기본 키 값으로 사용해 DB에 저장 -> INSERT INTO BOARD...
    // JPA는 시퀀스에 접근하는 횟수를 줄이기 위해 allocationSize를 지정
    //  설정한 값만큼 한 번에 시퀀스 값 증가시키고 그만큼 메모리에 시퀀스 값을 할당
    //  ex. initial = 1, Size = 50
    //      1. 시퀀스 값을 50 증가(DB SEQ: 51), 1~51까지 메모리에서 식별자 할당
    //      2. 52부터 DB에 시퀀스 증가, 51~101까지 메모리에서 식별자 할당
    // 이 방법은 시퀀스 값을 선점해서 여러 JVM이 동시 동작해도 기본 키 값 충돌하지 않음
    //      트랙잭션 1:
    //          em.persist()
    //              Hibernate:
    //                  call next value for BOARD_SEQ // value 1
    //              Hibernate:
    //                  call next value for BOARD_SEQ // value 2 ~ 51
    //      트랜잭션 2:
    //          em.persist()
    //              Hibernate:
    //                  call next value for BOARD_SEQ // value 52 ~ 101
    //      해당 예제 Ref. https://passionate.tistory.com/59
    // 단, DB 직접 접근해서 데이터 등록 시 시퀀스 값이 한번에 많이 증가 -> 부담스러우면 Size 값을 1로 설정
    //      SEQUENCE 최적화 관련 Ref. https://mgyo.tistory.com/823
)
public class Board {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "BOARD_SEQ_GENERATOR"   // 식별자 생성기 이름과 매핑 필요
    )
    private Long id;

    protected Board() {}

    public Long getId() {
        return id;
    }
}
