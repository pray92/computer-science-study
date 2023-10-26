package jpabook.highlevelmapping.composite.idclass.id;

import java.io.Serializable;

public class GrandChildId implements Serializable {
    private String parent;  // Child.parent 매핑
    private String childId; // Child.childId 매핑

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt( parent + childId);
    }
}
