package jpabook.variousmapping.manytomany.composite;

import javax.persistence.*;

@Entity
@IdClass(MemberProductId.class)
public class MemberProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private MemberComp member;      // MemberProductId.member와 연결

    @Id
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductComp product;      // MemberProductId.product와 연결

    private int orderAmount;

    public MemberComp getMember() {
        return member;
    }

    public void setMember(MemberComp member) {
        this.member = member;
    }

    public ProductComp getProduct() {
        return product;
    }

    public void setProduct(ProductComp product) {
        this.product = product;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
