package jpabook.jpastart;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
        // EntityManager Factory - 생성
        // persistence.xml 설정 정보를 사용해 EntityManager Factory 생성해야 함
        // 이때 Persistence 클래스 사용 -> EntityManager Factory를 생성해 JPA 사용하도록 함
        //
        // META-INF/persistence.xml에서 이름이 'jpabook'인 영속성 유닛(Persistence Unit)을 찾아 Factory 생성
        // 이때 설정 정보를 읽어서 JPA 동작을 위한 기반 객체 생성 후, 구현체에 따라서 DB Connection Pool도 생성 -> 비용이 큶
        // 따라서 EntityManager Factory는 애플리케이션 전체에서 한번만 생성하고 공유해서 사용해야 함
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        // EntityManager - 생성
        // Factory에서 EntityManager 생성
        // JPA의 대부분 기능은 EntityManager가 제공 -> 엔티티를 DB에 CRUD
        //
        // INFO: EntityManager는 DB Connection과 밀접한 관계가 있으므로 Thread 간에 공유되거나 재사용하면 안됨
        EntityManager em = emf.createEntityManager();

        // Transaction - 관리
        EntityTransaction tx = em.getTransaction(); // Transaction API
        try {
            // JPA 사용 시 반드시 Transaction 안에서 데이터 변경해야 함, 없이하면 예외 발생
            tx.begin();         // Transaction - 시작
            logic(em);          // Logic
            tx.commit();        // Transaction - Commit(저장)
        } catch (Exception e) {
            tx.rollback();      // Transaction - 예외 발생 시 Rollback
        } finally {
            // 마지막으로 사용이 끝난 EntityManager는 반드시 종료
            em.close();
        }
        // Factory도 마찬가지
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

    /**
     * JPQL :
     * JPA는 SQL을 추상화한 JPQL라는 객체지향 쿼리 언어를 제공
     * SQL과 문법이 거의 유사해서 SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 등 사용 가능
     *
     * 차이점 :
     * JPQL - 엔티티 객체, 쉽게 말해 클래스와 필드를 대상으로 쿼리
     * SQL - DB 테이블 대상으로 쿼리
     */
    private void jpql(EntityManager em) {
        // 쿼리 객체 생성
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

        // 해당 메서드 호출을 통해 데이터 집합 -> 엔티티 컨테이너
        List<Member> members = query.getResultList();
    }

}
