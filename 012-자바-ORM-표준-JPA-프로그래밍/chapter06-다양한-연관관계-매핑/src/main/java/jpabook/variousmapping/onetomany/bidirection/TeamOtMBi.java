package jpabook.variousmapping.onetomany.bidirection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TeamOtMBi {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<MemberOtMBi> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberOtMBi> getMembers() {
        return members;
    }
}
