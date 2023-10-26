package jpabook.highlevelmapping.jointable.onetomany;

import javax.persistence.*;

@Entity
public class Child {

    @Id @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;

    private String name;

    // 양방향 매핑을 원하면 아래 코드 추가
    @ManyToOne
    private Parent parent;
}
