package jpabook.highlevelmapping.composite.embeddedid.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ChildId implements Serializable {
    private String parentId;  // @MapsId("parentId")로 매핑

    @Column(name = "CHILD_ID")
    private String id;

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt( parentId + id);
    }
}
