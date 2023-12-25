package jpabook.variousmapping.manytomany.composite;

import java.io.Serializable;

/**
 * 복합 기본 키
 * - JPA에서 복합 키 사용하려면 별도의 식별자 클래슬르 만들어야 함
 * - 그리고 @IdClass를 사용해 식별자 클래스 지정
 * - 복합키를 위한 식별자 클래스는 아래 특징있음
 *      1. 복합키는 별도의 식별자 클래스로 만들어야 함
 *      2. Serializable로 구현해야 함 -> 하이버네이트 구현체는 객체를 임시로 직렬화해 메모리에 올려두는 작업을 하기 때문
 *          참고: https://www.inflearn.com/questions/16570/%EB%B2%84%EA%B7%B8-%EB%AC%B8%EC%9D%98%EB%93%9C%EB%A0%A4%EB%B4%85%EB%8B%88%EB%8B%A4
 *      3.equals와 hashCode를 구현해야 함 -> 영속성 컨텍스트는 엔티티 식별자를 키로 사용해서 엔티티를 관리하기 때문
 *          참고: https://modimodi.tistory.com/14
 *      4. 기본 생성자가 있어야 함
 *      5. 식별자 클래스는 public이어야 함
 *      6. @EmbeddedId를 사용하는 방법도 존재
 *  DB 설계에선 부모 테이블의 기본 키를 받아서 자식 테이블의 기본 키 + 외래 키로 사용하는 것을 '식별 관계'라 함
 */
public class MemberProductId implements Serializable {

    private String member;  // MemberProduct.member와 연결
    private String product;  // MemberProduct.product와 연결

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return member.hashCode() + product.hashCode();
    }
}
