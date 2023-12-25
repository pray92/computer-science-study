package jpabook.proxy.transitive;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Child> children = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<Child> getChildren() {
        return children;
    }

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            insert();
            tx.commit();

            em.clear();

            tx = em.getTransaction();
            tx.begin();
            query();
            tx.commit();

            tx = em.getTransaction();
            tx.begin();
            delete();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * - 기존에 JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 했음
     * - 그래서 child도 필수적으로 persist를 줘야했음
     * - 영속성 전이를 통해 parent(영속성 전이 속성을 지정한 엔티티)만 영속 상태만 만들어도 연관 엔티티도 한 번에 영속 상태로 만들 수 있음
     */
    private static void insert() {
        Child child1 = new Child();
        Child child2 = new Child();
        /*em.persist(child1);
        em.persist(child2);*/


        Parent parent = new Parent();
        child1.setParent(parent);
        child2.setParent(parent);
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        em.persist(parent);
    }

    private static void query() {
        Parent parent = em.find(Parent.class, 1L);
        parent.getChildren().forEach(child -> System.out.println("child.getId() = " + child.getId()));
    }

    /**
     * - 삭제도 마찬가지로 전이가 가능
     * - 그러나 이 경우 CascadeType.REMOVE도 추가 설정해야 함
     * - 그렇지 않으면 em.remove 대상 엔티티만 삭제되고, 연관 테이블에 걸려 있는 외래 키 제약조건으로 인해 DB에서 외래키 무결성 예외가 발생
     *
     */
    private static void delete() {
        Parent parent = em.find(Parent.class, 1L);
        em.remove(parent);
    }
}
