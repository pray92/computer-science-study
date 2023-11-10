package jpabook.oopquerylanguage.inheritance;

import javax.persistence.*;

import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private int price;

    private int stockQuantity;

    public Item() {
    }

    public Item(int price, int stockQuantity) {
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }


    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {


        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            query();
            tx.commit();
            em.clear();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * 부모 엔티티를 조회하면 그 자식 엔티티도 함께 조회함
     * JOINED 전략 수행 시 외부 조인 수행
     */
    private static void query() {
        List resultList = em.createQuery("select i from Item i").getResultList();

        // TYPE: 엔티티의 상속 구조에서 조회 대상을 특정 자식 타입으로 한정 시 사용(SINGLE_TABLE 전략에서만 동작)
        resultList = em.createQuery("select i from Item i where type(i) IN (Book, Movie)").getResultList();

        // TREAT(2.1): 자바의 타입 캐스팅과 유사, 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
        resultList = em.createQuery("select i from Item i where treat(i as Book).author = 'kim'").getResultList();
    }
}
