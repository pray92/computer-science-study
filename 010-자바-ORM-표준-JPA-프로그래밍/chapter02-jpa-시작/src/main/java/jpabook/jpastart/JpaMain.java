package jpabook.jpastart;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
        // EntityManager Factory - Create
        // Create factory based on persistence.xml
        // Find persistence unit from META-INF/persistence.xml named 'jpabook', then create entity manager factory.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        // EntityManager - Create
        // Create manager from Factory
        // It may CRUD entities using manager.
        // Entity Manager connects with DB while maintaining datasource(DB Connection) internally.
        //
        // INFO:
        // Since Entity Manager is firmly related with DB Connection, it must not be shared or reused among threads.
        EntityManager em = emf.createEntityManager();

        // Transaction - Acquisition
        EntityTransaction tx = em.getTransaction();
        try {
            // Data must be changed in transaction if using JPA.
            // If not, exception is thrown.
            // If logic worked well, transaction is commit well.
            // If exception is thrown, transaction needs to be rollback.
            tx.begin();         // Transaction - Begin
            logic(em);          // Logic
            tx.commit();        // Transaction - Commit
        } catch (Exception e) {
            tx.rollback();      // Transaction - Rollback
        } finally {
            // Entity Manager must be closed after use.
            em.close();
        }
        // Factory either.
        emf.close();
    }

    private static void logic(EntityManager em) {
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("Jisu");
        member.setAge(32);

        // Enrollment
        em.persist(member);

        // Update
        member.setAge(30);

        // Read
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember = " + findMember.getUsername() + ", age = " + findMember.getAge());

        // List-up
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size() = " + members.size());

        // Delete
        em.remove(member);
    }

}
