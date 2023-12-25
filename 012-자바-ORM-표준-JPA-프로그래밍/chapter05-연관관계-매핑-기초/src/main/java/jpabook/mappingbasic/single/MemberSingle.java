package jpabook.mappingbasic.single;

import javax.persistence.*;

@Entity
public class MemberSingle {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToOne(                     // 다대일 매핑 정보, 필수 사용
        // optional                 // false 설정 시 연관된 엔티티가 항상 있어야 함
        // fetch                    // 글로벌 페치 전략 설정 -> EAGER, LAZY
        // cascade                  // 영속성 전이 기능
        // targetEntity             // 연관 엔티티 타입 정보 설정, 거의 사용하지 않음
                                    // ex.
                                    // 지네릭 미사용 시 사용
                                    // @OneToMany(targetEntity=Member.class)
                                    // List members;
    )
    @JoinColumn(                    // 외래 키 매핑 시 사용
        name = "TEAM_ID"            // 매핑할 외래 키 이름 지정(Default: {필드 명}_{참조 테이블 기본키 컬럼명})
        // referencedColumnName     // 외래 키가 참조하는 대상 테이블의 컬럼 명
        // foreignKey(DDL)          // 외래 키 제약조건 직접 지정, 테이블 생성 시에만 사용
        // 그 외                     // @Column과 동일, 코드를 직접 봐서 확인
    )
    private TeamSingle team;

    public MemberSingle(){}

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

    public TeamSingle getTeam() {
        return team;
    }

    public void setTeam(TeamSingle team) {
        this.team = team;
    }
}
