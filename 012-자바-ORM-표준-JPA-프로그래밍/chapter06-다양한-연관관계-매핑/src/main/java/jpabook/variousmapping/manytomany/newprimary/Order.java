package jpabook.variousmapping.manytomany.newprimary;


import javax.persistence.*;

/**
 * 새로운 기본 키 생성 전략
 * - DB에서 자동으로 생성해주는 대리 키를 Long 값으로 사용하는 것
 * - 간편하고 거의 영구히 쓸수 있으며 비즈니스에 의존하지 않음
 * - 무엇보다 복합키처럼 따로 equals, hashCode를 선언할 필요가 없으며 식별자 클래스로 따로 만들 필요 없음
 * - DB 설계에선 이처럼 받아온 식별자는 외래 키로만 사용하고 새로운 식별자를 추가하는 것을 '비식별 관계'라 함
 */
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int orderAmount;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
