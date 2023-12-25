package jpabook.variousmapping.onetoone;

import javax.persistence.*;

/**
 * 대상 테이블에 외래 키
 *
 * 단방향
 * - JPA는 일대일 관계 중 대상 테이블에 외래 키가 있는 단방향 관계 미지원
 *  - 이런 모양으로 매핑할 수 있는 방법도 없음
 *  - 이때는 단방향 관계를 Locker -> Member 또는 양방향 관계로 만들고 Locker를 연관관계 주인으로 설정
 * - JPA 2.0부터 일대다 단방향 관계에서 대상 테이블에 외래 키가 있는 매핑 허용, 일대일 단방향은 허용 X
 *
 * 양방향
 * - 대상 테이블에 외래 키를 두고 싶으면 대상 엔티티인 Locker를 연관관계 주인으로 만들면 됨
 *
 * 일대일 관계 정리명
 * - 주 테이블에 외래 키
 *  - 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
 *  - 객체지향 개발자가 선호
 *  - JPA 매핑 편리
 *  - 장점: 주 테이블만 조회해도 대상 테이블에 데이터 유무 확인가능
 *  - 단점: 값이 없으면 외래 키에 null 허용
 * - 대상 테이블에 외래 키
 *  - 대상 테이블에 외래 키 존재
 *  - 전통적인 DB 개발자 선호
 *  - 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경 시 테이블 구조 유지
 *  - 단점: 프록시 기능의 한계로 지연 로딩 설정해도 항상 즉시 로딩됨
 *         bytecode instrumentation으로 해결할 수 있긴함
 *  참고: https://velog.io/@conatuseus/JPA-%EB%8B%A4%EC%96%91%ED%95%9C-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84-%EB%A7%A4%ED%95%91#%EC%96%91%EB%B0%A9%ED%96%A5-1
 */
@Entity
public class Locker {

    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
