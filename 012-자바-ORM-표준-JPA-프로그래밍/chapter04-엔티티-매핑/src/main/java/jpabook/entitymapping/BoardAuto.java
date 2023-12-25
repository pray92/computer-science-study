package jpabook.entitymapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BoardAuto {

    /**
     * AUTO:
     * DB 방언에 따라 IDENTITY, SEQUENCE, TABLE 전략 중 하나를 자동 선택
     *      Oracle -> SEQUENCE
     *      MysQL -> IDENTITY
     * @GeneratedValue의 default strategy
     * DB 변경해도 코드를 수정할 필요가 없음 -> 특히 키 생성 전략이 안정해진 초기 단계 및 프로토타입에 편리하게 사용
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
