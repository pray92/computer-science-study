package jpabook.highlevelmapping.jointable.manytomany;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    @Column(name = "PARENT_ID")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "PARENT_CHILD",
            joinColumns = @JoinColumn(name = "PARENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CHILD_ID")
    )
    private List<Child> child = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();


        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Parent parent1 = new Parent();
            parent1.setName("parent1");
            Parent parent2 = new Parent();
            parent2.setName("parent2");


            Child child1 = new Child();
            child1.setName("child1");
            Child child2 = new Child();
            child2.setName("child2");

            parent1.setChild(List.of(child1, child2));
            child1.setParent(List.of(parent1, parent2));

            em.persist(parent1);
            em.persist(parent2);
            em.persist(child1);
            em.persist(child2);

            tx.commit();
            
            em.clear();
            
            tx.begin();
            Parent parent = em.find(Parent.class, 1L);
            parent.getChild().forEach(child -> {
                System.out.println("child.getName() = " + child.getName());
            });
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
