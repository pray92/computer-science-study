package jpabook.variousmapping.manytomany.bidirection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductBi {

    @Id @Column(name = "PRODUCT_ID")
    private String id;

    private String name;

    @ManyToMany(mappedBy = "products")
    private List<MemberMtMBi> members = new ArrayList<>();

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

    public List<MemberMtMBi> getMembers() {
        return members;
    }
}
