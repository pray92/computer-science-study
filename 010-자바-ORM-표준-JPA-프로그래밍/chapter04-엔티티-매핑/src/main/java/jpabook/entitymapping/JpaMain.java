package jpabook.entitymapping;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    /**
     * 대표 에너테이션
     * 1. 객체와 테이블 매핑: @Entity, @Table
     * 2. 기본 키 매핑: @Id
     * 3. 필드와 컬럼 매핑: @Column
     * 4. 연관관계 매핑: @ManyToOne, @JoinColumn
     */
	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            table();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
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

    private static void idStrategy() {
        Member member = new Member();
        member.setUsername("Jisu");
        member.setAge(32);
        em.persist(member);
        System.out.println("member.getId() = " + member.getId());
        
        Member findMember = em.find(Member.class, member.getId());
        System.out.println("findMember.getId() = " + findMember.getId());
    }

    private static void sequence() {
        Board board1 = new Board();
        Board board2 = new Board();
        Board board3 = new Board();
        Board board4 = new Board();
        Board board5 = new Board();
        Board board6 = new Board();

        em.persist(board1);
        em.persist(board2);
        em.persist(board3);
        em.persist(board4);
        em.persist(board5);
        em.persist(board6);

        System.out.println("board1.getId() = " + board1.getId());
        System.out.println("board2.getId() = " + board2.getId());
        System.out.println("board3.getId() = " + board3.getId());
        System.out.println("board4.getId() = " + board4.getId());
        System.out.println("board5.getId() = " + board5.getId());
        System.out.println("board6.getId() = " + board6.getId());
    }

    private static void table() {
        BoardTable boardTable = new BoardTable();
        em.persist(boardTable);
        System.out.println("boardTable.getId() = " + boardTable.getId());
    }
}
