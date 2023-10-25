package jpabook.variousmapping.manytomany.newprimary;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

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

    public List<Order> getOrders() {
        return orders;
    }
}
