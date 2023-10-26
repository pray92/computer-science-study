package jpabook.highlevelmapping.composite.embeddedid.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GrandChildId implements Serializable {

    private ChildId childId;    // @MapsId("childId")와 매핑

    @Column(name = "GRANDCHILD_ID")
    private String id;

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return childId.hashCode() + id.hashCode();
    }
}
