package jpabook.oopquerylanguage.entity;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded private Address address;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Order() {
    }

    public Order(int orderAmount, Address address, Member member, Product product) {
        this.orderAmount = orderAmount;
        this.address = address;
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }
    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
}
