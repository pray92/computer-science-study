package jpabook.highlevelmapping.composite.embeddedid.id;

import javax.persistence.*;

@Entity
public class Child {
    @EmbeddedId
    private ChildId id;

    @MapsId("parentId") // ChildId.parentId 매핑,
                        // @EmbeddedId로 식별 관계 구성 시 @MapsId 사용
                        // @MapsId는 외래 키와 매핑한 연관관계를 기본 키에도 매핑할 때 사용
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;

    private String name;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public ChildId getId() {
        return id;
    }

    public void setId(ChildId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
