package jpabook.springdatajpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    @Setter
    private Order order;

    @Embedded
    private Address Address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery(Order order, jpabook.springdatajpa.domain.Address address, DeliveryStatus status) {
        this.order = order;
        Address = address;
        this.status = status;
    }


}
