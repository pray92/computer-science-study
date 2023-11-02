package jpabook.proxy.orphan;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
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

            em.clear();

            tx = em.getTransaction();
            tx.begin();
            query();
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void insert() {
        Child child1 = new Child();
        Child child2 = new Child();
        em.persist(child1);
        em.persist(child2);

        Parent parent = new Parent();
        child1.setParent(parent);
        child2.setParent(parent);
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        em.persist(parent);
    }

    private static void query() {
        Parent parent = em.find(Parent.class, 3L);
        parent.getChildren().forEach(child -> System.out.println("child.getId() = " + child.getId()));
    }

    /**
     * - 해당 코드를 실행한 SQL 결과문은 DELETE FROM CHILD WHERE ID=?라고 함
     *   (실제로 코드를 돌려보면 작동하진 않음, em.clear를 했는데도 동작하지 않음)
     * - orphanRemoval = true 옵션을 통해 컬렉션에서 엔티티 제거 시 DB의 데이터도 삭제됨
     * - 고아 객체 제거 기능은 영속성 컨텍스트를 플러시할 때 적용되므로 플러시 시점에 DELETE SQL이 실행리
     */
    private static void delete() {
        Parent parent = em.find(Parent.class, 3L);
        parent.getChildren().remove(0);
        em.persist(parent);
    }
}
