package jpabook.variousmapping.onetomany.bidirection;

import jpabook.variousmapping.manytoone.Locker;

import javax.persistence.*;

/**
 * 일대다 양방향
 * - 존재하지 않는 개념, 대신 다대일 양방향 매핑 사용
 *  - 양방향 매핑에서 @OneToMany는 연관관계 주인이 될 수 없음
 *  - 왜냐하면 RDB 특성상 일대다, 다대일 관계는 항상 다 쪽에 외래 키가 있음
 *  - 따라서 연관관계 주인을 항상 @ManyToOne을 사용한 곳 -> @ManyToOne에 mappedBy 속성이 없는 이유
 */
@Entity
public class MemberOtMBi {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    /**
     * - 일대다 양방향 매핑이 완전히 불가능한 것은 아님
     * - 단방향 매핑 반대편에 같은 외래 키를 사용하는 다대일 단방향 매핑을 읽기전용으로 추가하면 됨
     * - 읽기 전용으로 하는 이유는 양쪽 다 같은 키를 관리해서 문제가 발생할 수 있기 때문
     * - 해당 방법은 일대다 단방향 매핑 반대편에 다대일 단방향 매핑을 읽기전용으로 추가해서 일대다 양방향처럼 보이도록 함
     *      -> 일대다 단방향 매핑이 가지는 단점을 그대로 가짐
     * - 따라서 다대일 양방향 매핑을 권고
     */
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private TeamOtMBi team;

    /**
     * 일대일 연관관계
     * - MEMBER 테이블이 외래 키를 가지므로 Member.locker가 연관관계 주인
     */
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
