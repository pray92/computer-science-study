package jpabook.oopquerylanguage.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.oopquerylanguage.dto.UserDTO;
import jpabook.oopquerylanguage.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import static jpabook.oopquerylanguage.entity.QMember.member;

public class QueryDSLEx {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    // 4버전 이후로 Factory 제공
    static JPQLQueryFactory query = new JPAQueryFactory(() -> em);

    public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            insert();
            tx.commit();

            em.clear();

            tx = em.getTransaction();
            tx.begin();
            queryDSL();
            tx.commit();

            em.clear();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    private static void insert() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 20);

        em.persist(member1);
        em.persist(member2);

        Product product1 = new Product("product1", 1000, 3);
        Product product2 = new Product("product2", 2000, 1);
        em.persist(product1);
        em.persist(product2);

        Order order = new Order(1, null, member1, product1);
        em.persist(order);

        Team teamA = new Team("teamA");
        member1.setTeam(teamA);
        member2.setTeam(teamA);
        em.persist(teamA);

        Member member3 = new Member("member3", 30);
        Team teamB = new Team("teamB");
        member3.setTeam(teamB);
        em.persist(teamB);
        em.persist(member3);
    }


    /**
     * QueryDSL에서 사용하는 Q Class 생성을 위해 처음에 compile을 통해 해당 클래스를 생성해야 함
     * out, build 경로에 생성됨
     */
    private static void queryDSL() {
        QMember qMember = new QMember("m");         // 생성되는 JPQL 별칭이 m
        List<?> results = query.from(member)                // QMember에 인스턴스 초기화 이슈로 인한 최적화를 위해 member라는 static 필드를 추가
                // import static을 통해 편리하게 사용 가능
                .where(member.username.contains("member"))
                .orderBy(member.username.desc())
                .fetch();
        results.forEach(member -> System.out.println("member = " + member));


        query.select(member)
                .from(member)
                .where(member.isOld()).fetch()
            .forEach(member -> System.out.println("member = " + member));

    }

}
