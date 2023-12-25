package jpabook.highlevelmapping.singletable;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*@DiscriminatorColumn(name = "DTYPE")*/ // 생략 가능, name(default): "DTYPE"
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private int price;
}
