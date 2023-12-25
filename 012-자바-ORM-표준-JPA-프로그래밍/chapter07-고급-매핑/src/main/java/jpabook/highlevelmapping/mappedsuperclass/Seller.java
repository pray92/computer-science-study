package jpabook.highlevelmapping.mappedsuperclass;

import javax.persistence.Entity;

@Entity
public class Seller extends BaseEntity {

    private String shopName;
}
