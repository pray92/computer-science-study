package jpabook.oopquerylanguage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockAmount;

    @OneToMany(mappedBy = "product")
    private List<Order> order = new ArrayList<>();

    public Product() {
    }

    public Product(String name, int price, int stockAmount) {
        this.name = name;
        this.price = price;
        this.stockAmount = stockAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public List<Order> getOrder() {
        return order;
    }
}
