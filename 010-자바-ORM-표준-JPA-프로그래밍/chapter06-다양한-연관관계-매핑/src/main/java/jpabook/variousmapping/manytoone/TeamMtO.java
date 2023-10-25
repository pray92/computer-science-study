package jpabook.variousmapping.manytoone;

import javax.persistence.*;
import java.util.List;

@Entity
public class TeamMtO {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<MemberMtO> members;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberMtO> getMembers() {
        return members;
    }
}
