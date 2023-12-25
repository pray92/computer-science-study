package jpabook.variousmapping.manytomany.bidirection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MemberMtMBi {

    @Id @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToMany
    @JoinTable(
        name = "MEMBER_PRODUCT_BI",
        joinColumns = @JoinColumn(name = "MEMBER_ID"),
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private List<ProductBi> products = new ArrayList<>();

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

    public List<ProductBi> getProducts() {
        return products;
    }

    public void addProduct(ProductBi product) {
        products.add(product);
        product.getMembers().add(this);
    }
}
