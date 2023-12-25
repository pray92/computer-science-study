package jpabook.highlevelmapping.jointable.manytoone;

import javax.persistence.*;

@Entity
public class Child {

    @Id @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;

    private String name;

    @ManyToOne(optional = false/* not null 보장 */)
    @JoinTable(
            name = "PARENT_CHILD",
            joinColumns = @JoinColumn(name = "CHILD_ID"),
            inverseJoinColumns = @JoinColumn(name = "PARENT_ID")
    )
    private Parent parent;
}
