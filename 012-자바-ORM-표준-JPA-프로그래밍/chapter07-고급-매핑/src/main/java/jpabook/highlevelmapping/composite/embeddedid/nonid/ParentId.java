package jpabook.highlevelmapping.composite.embeddedid.nonid;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ParentId implements Serializable {
    @Column(name = "PARENT_ID1")
    private String id1; // Parent.id1 매핑
    
    @Column(name = "PARENT_ID2")
    private String id2; // Parent.id2 매핑

    public ParentId() {
    }

    public ParentId(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt( id1 + id2);
    }
}
