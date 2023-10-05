package jpabook.variousmapping;

import jpabook.variousmapping.Member;
import jpabook.variousmapping.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    private static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            save(em);
            find(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * 일대다 단방향 매핑의 단점
     *
     * 매핑한 객체가 관리하는 외래키가 다른 테이블에 존재.
     * 본인 테이블 외에 외래키가 있으면, 엔티티의 저장과 연관관계 처리를 INSERT SQL 한번으로 끝낼 수 있으나
     * 다른 테이블에 외래키가 있으면 연관관계 처리를 위한 UPDATE SQL을 추가로 실행해야 함.
     *
     * 이러한 이유로, 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하는 것이 좋음.
     * 일대다 단방향 매핑 사용 시 엔티티를 매핑한 테이블이 아닌 다른 테이블의 외래키를 관리해야 함.
     * 이는 성능 문제도 있으나 관리도 부담스러움.
     * 문제를 해결하는 좋은 방법은 일대다 단방향 매핑 대신, 다대일 양방향 매핑을 사용하는 것.
     * 다대일 양방향 매핑은 관리해야 하는 외래 키가 본인 테이블에 있음.
     * 따라서 일대다 단방향 매핑 같은 문제가 발생하지 않음.
     *
     * @param em
     */
    private static void testSave(EntityManager em) {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        Team team1 = new Team("team1");
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);

        em.persist(member1);        // INSERT-member1
        em.persist(member2);        // INSERT-member2
        em.persist(team1);          // INSERT-team1, UPDATE-member1.fk, UPDATE-member2.fk
    }

    /*private static void save(EntityManager em) {
        Product productA = new Product();
        productA.setId("productA");
        productA.setName("상품A");
        em.persist(productA);

        Member member1 = new Member();
        member1.setUsername("회원1");
        member1.getProducts().add(productA);
        productA.getMembers().add(member1);
        em.persist(member1);
    }

    private static void find(EntityManager em) {
        Member member = em.find(Member.class, 1L);
        List<Product> products = member.getProducts();
        products.forEach(product -> System.out.println("product.getName() = " + product.getName()));
    }

    private static void findInverse(EntityManager em) {
        Product product = em.find(Product.class, "productA");
        List<Member> members = product.getMembers();
        members.forEach(member -> System.out.println("member = " + member.getUsername()));
    }*/

    private static void save(EntityManager em) {
        // 회원 저장
        Member member1 = new Member();
        //member1.setId(1L);
        member1.setUsername("회원1");
        em.persist(member1);

        // 상품 저장
        Product productA = new Product();
        productA.setId("productA");
        productA.setName("상품1");
        em.persist(productA);

        // 회원상품 저장
        /*MemberProduct memberProduct = new MemberProduct();
        memberProduct.setMember(member1);
        memberProduct.setProduct(productA);
        memberProduct.setOrderAmount(2);

        em.persist(member1);*/

        // 주문 저장
        Orders order = new Orders();
        order.setMember(member1);
        order.setProduct(productA);
        order.setOrderAmount(2);
        em.persist(order);
    }

    private static void find(EntityManager em) {
        Orders order = em.find(Orders.class, 1L);

        Member member = order.getMember();
        Product product = order.getProduct();

        System.out.println("member = " + member.getUsername());
        System.out.println("product = " + product.getName());
        System.out.println("order = " + order.getOrderAmount());
    }

}
