package jpabook.entitymapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Board board = new Board();
            em.persist(board);
            System.out.println("board.id = " + board.getId());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
        em.close();
        emf.close();
    }

}
