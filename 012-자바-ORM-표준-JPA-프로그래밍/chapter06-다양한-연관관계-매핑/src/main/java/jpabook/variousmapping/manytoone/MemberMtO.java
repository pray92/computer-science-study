package jpabook.variousmapping.manytoone;

import javax.persistence.*;


/**
 * 다대일 연관관계
 * - 양방향은 외래 키가 있는 쪽이 연관관계의 주인
 *  - 일대다나 다대일엔 항상 다에 외래키가 있음
 *  - 여기선 다쪽인 Member 테이블이 외래 키를 가지므로 Member.team이 연관관계 주인
 *  - JPA는 외래 키 관리 시 연관관계 주인만 사용, 주인이 아닌 Team.members는 조회를 위한 JPQL or 객체 그래프 탐색시 사용
 * - 양방향 연관관계는 항상 서로 참조해야 함
 *  - 한쪽만 참조 시 양방향 연관관계가 성립되지 않음
 *  - 항상 서로 참조하게 하려면 연관관계 편의 메서드를 작성하는 것이 좋음 -> setTeam, addMember
 *  - 편의 메서드는 한 곳에만 작성하거나 양쪽 다 작성 가능한데, 양쪽에 작성하면 무한루프 가능성이 있으므로 주의
 */
@Entity
public class MemberMtO {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private TeamMtO team;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TeamMtO getTeam() {
        return team;
    }

    public void setTeam(TeamMtO team) {
        this.team = team;

        // 무한루프 빠지지 않도록 체크
        if(!team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }
}
