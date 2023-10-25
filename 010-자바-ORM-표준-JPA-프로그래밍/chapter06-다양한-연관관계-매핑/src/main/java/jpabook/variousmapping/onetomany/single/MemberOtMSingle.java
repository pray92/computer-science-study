package jpabook.variousmapping.onetomany.single;

import javax.persistence.*;

@Entity
public class MemberOtMSingle {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

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
