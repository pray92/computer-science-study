package jpabook.mappingbasic.single;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TeamSingle {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    public TeamSingle() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
