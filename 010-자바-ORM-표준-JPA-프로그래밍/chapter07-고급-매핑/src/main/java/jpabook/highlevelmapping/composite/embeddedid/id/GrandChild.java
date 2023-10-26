package jpabook.highlevelmapping.composite.embeddedid.id;

import javax.persistence.*;

@Entity
public class GrandChild {

    @EmbeddedId
    private GrandChildId id;
    @MapsId("childId")  // GrandChildId.childId 매핑
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID"),
            @JoinColumn(name = "CHILD_ID")
    })
    private Child child;

    private String name;

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
