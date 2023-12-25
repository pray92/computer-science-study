package jpabook.highlevelmapping.jointable.manytomany;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Child {

    @Id @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "PARENT_CHILD",
            joinColumns = @JoinColumn(name = "CHILD_ID"),
            inverseJoinColumns = @JoinColumn(name = "PARENT_ID")
    )
    private List<Parent> parent = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parent> getParent() {
        return parent;
    }

    public void setParent(List<Parent> parent) {
        this.parent = parent;
    }
}
