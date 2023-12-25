package jpabook.variousmapping.manytomany.composite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductComp {

    @Id @Column(name = "PRODUCT_ID")
    private String id;

    private String name;

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> members = new ArrayList<>();

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

    public List<MemberProduct> getMembers() {
        return members;
    }
}
