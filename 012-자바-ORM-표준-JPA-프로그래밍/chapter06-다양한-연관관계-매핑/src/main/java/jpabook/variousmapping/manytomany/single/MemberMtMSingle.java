package jpabook.variousmapping.manytomany.single;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 다대다 연관관계
 * - RDB는 정규화된 테이블로 2개로 다대다 관계를 표현할 수 없음
 * - 그래서 보통 다대다 관계를 일대다, 다대일 관계로 풀어내는 '연결 테이블'을 사용(img_8.png 참고)
 * - 문제는 객체는 테이블과 달리 다대다 관계를 만들 수 있음
 */
@Entity
public class MemberMtMSingle {

    @Id @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToMany
    @JoinTable(                                                 // 연결 테이블 매핑 시 사용
        name = "MEMBER_PRODUCT",                                // 연결 테이블 지정
        joinColumns = @JoinColumn(name = "MEMBER_ID"),          // 현 방향인 회원과 매핑할 조인 컬럼 정보
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")   // 반대 방향인 상품과 매핑할 조인 컬럼 정보
    )
    private List<ProductSingle> products = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ProductSingle> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSingle> products) {
        this.products = products;
    }
}
