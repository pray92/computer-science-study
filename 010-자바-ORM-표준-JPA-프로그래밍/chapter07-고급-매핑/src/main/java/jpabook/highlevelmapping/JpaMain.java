package jpabook.highlevelmapping;

import jpabook.highlevelmapping.composite.idclass.nonid.Parent;
import jpabook.highlevelmapping.composite.idclass.nonid.ParentId;
import jpabook.highlevelmapping.onetoone.Board;
import jpabook.highlevelmapping.onetoone.BoardDetail;

import javax.persistence.*;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            //idClassSaveAndFind();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void idClassSaveAndFind() {
        Parent parent = new Parent();
        parent.setId1("myId1");
        parent.setId2("myId2");
        parent.setName("parentName");
        em.persist(parent);

        ParentId parentId = new ParentId("myId1", "myId2");
        Parent findParent = em.find(Parent.class, parentId);
        System.out.println("findParent.getName() = " + findParent.getName());
    }

    private static void oneToOneSave() {
        Board board = new Board();
        board.setTitle("제목");
        em.persist(board);

        BoardDetail boardDetail = new BoardDetail();
        boardDetail.setContent("내용");
        boardDetail.setBoard(board);
        em.persist(boardDetail);
    }

}
