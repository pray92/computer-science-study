package jpabook.highlevelmapping.mappedsuperclass;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;
}
