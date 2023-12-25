package jpabook.mappingbasic.bidirection;

import javax.persistence.*;

@Entity
public class MemberBi {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private TeamBi team;

    public MemberBi() {
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

    public TeamBi getTeam() {
        return team;
    }

    public void setTeam(TeamBi team) {
        this.team = team;
    }
}
