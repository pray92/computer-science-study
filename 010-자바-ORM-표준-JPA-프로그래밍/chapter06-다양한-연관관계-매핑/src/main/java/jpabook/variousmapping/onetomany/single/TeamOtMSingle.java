package jpabook.variousmapping.onetomany.single;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Entity
public class TeamOtMSingle {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")   // MEMBER 테이블의 TEAM_ID(FK)
    private List<MemberOtMSingle> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberOtMSingle> getMembers() {
        return members;
    }
}
