package jpabook.springdatajpa;


import javax.persistence.*;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }


}
