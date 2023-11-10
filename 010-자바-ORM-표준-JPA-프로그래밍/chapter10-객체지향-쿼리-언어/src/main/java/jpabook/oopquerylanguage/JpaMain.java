package jpabook.oopquerylanguage;

import jpabook.oopquerylanguage.entity.Member;
import jpabook.oopquerylanguage.entity.Order;
import jpabook.oopquerylanguage.entity.Product;
import jpabook.oopquerylanguage.entity.Team;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
