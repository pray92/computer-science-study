package jpabook.variousmapping;

import jpabook.variousmapping.manytomany.bidirection.MemberMtMBi;
import jpabook.variousmapping.manytomany.bidirection.ProductBi;
import jpabook.variousmapping.manytomany.composite.MemberComp;
import jpabook.variousmapping.manytomany.composite.MemberProduct;
import jpabook.variousmapping.manytomany.composite.MemberProductId;
import jpabook.variousmapping.manytomany.composite.ProductComp;
import jpabook.variousmapping.manytomany.newprimary.Member;
import jpabook.variousmapping.manytomany.newprimary.Order;
import jpabook.variousmapping.manytomany.newprimary.Product;
import jpabook.variousmapping.manytomany.single.MemberMtMSingle;
import jpabook.variousmapping.manytomany.single.ProductSingle;
import jpabook.variousmapping.onetomany.single.MemberOtMSingle;
import jpabook.variousmapping.onetomany.single.TeamOtMSingle;

import javax.persistence.*;
import java.util.List;

/**
 * 다양한 연관관계 매핑
 *
 * 연관관계 매핑 시 다음 3가지를 고려
 * 1. 다중성
 *  - 다대일(ManyToOne), 일대다(OneToMany), 일대일(OneToOne), 다대다(ManyToMany)
 *  - 다중성 판단이 어려울 시 반대방향을 생각 -> 회원은 하나의 팀에만 소속되고, 팀은 여러 회원을 가질 수 있음(회원:팀=다:일)
 *  - 보통 다대일과 일대다를 가장 많이 사용하고, 다대다 관계는 거의 사용하지 않음
 * 2. 단방향, 양방향
 *  - 테이블은 외래 키 하나로 양방향 조인이 가능 -> 방향 개념 X
 *  - 객체는 참조용 필드를 가지고 있는 객체만 연관된 객체 조회 가능
 *  - 한쪽만 참조하는 것을 단방향, 양쪽 서로 참조하는 것을 양방향이라 함
 * 3. 연관관계 주인
 *  - DB는 외래 키 하나로 두 테이블의 연관관계를 맺으므로, 연관관계를 관리하는 포인트는 외래 키 하나
 *  - 객체는 A -> B, B -> A 2곳에서 서로 참조하므로, 연관관계를 관리하는 포인트는 2곳
 *  - JPA에서 두 객체 연관관계 중 하나를 정해서 DB 외행 키를 관리함 => 연관관계 주인
 *  - 따라서 둘 중 하나를 정해서 외래 키 관리해야 함
 *  - 이 경우, '외래 키를 가진 테이블'과 '매핑한 엔티티'가 외래 키를 관리하는게 효율적이라 이곳을 연관관계 주인으로 선택
 */
public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            /*tx.begin();
            otMSingleSave();
            tx.commit();*/

            /*tx.begin();
            mtMSingleSave();
            tx.commit();
            em.clear();

            tx = em.getTransaction();
            tx.begin();
            mtMSingleFind();
            tx.commit();*/

            /*tx.begin();
            mtMBiSave();
            tx.commit();
            em.clear();

            tx = em.getTransaction();
            tx.begin();
            mtMBiFind();
            tx.commit();*/

            /*tx.begin();
            compositeSaveAndFind();
            tx.commit();*/

            tx.begin();
            newPrimarySaveAndFind();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }


    /**
     * 일대다 연관관계
     * - 다대일 관계의 반대 방향
     * - 엔티티를 하나 이상 참조할 수 있어 Collection, List, Set, Map 중 하나를 사용해야 함
     *
     * 일대다 단방향 단점
     * - 매핑한 객체가 관리하는 외래 키가 다른 테이블에 있음
     * - 본인 테이블에 외래 키가 있으면 엔티티의 저장과 연관관계 처리를 INSERT 한번으로 처리 가능
     * - 하지만 다른 테이블에 외래 키가 있으면 연관관계 처리를 위한 UPDATE를 한번 더 실행해야 함
     *
     * 예시 설명
     * - Member 엔티티는 Team 엔티티를 모름
     * - 연관관계에 대한 정보는 Team의 members가 관리
     * - 따라서 Member 엔티티 저장 시 MEMBER 테이블에 TEAM_ID 외래 키에 아무 값도 저장X
     * - 대신 Team 엔티티 저장 시 Team.members 참조 값을 확인해 MEMBER 테이블의 TEAM_ID 외래 키를 업데이트
     */
    private static void otMSingleSave() {
        MemberOtMSingle member1 = new MemberOtMSingle();
        member1.setUsername("member1");
        MemberOtMSingle member2 = new MemberOtMSingle();
        member2.setUsername("member2");

        TeamOtMSingle team1 = new TeamOtMSingle();
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);

        em.persist(member1);        // INSERT member1
        em.persist(member2);        // INSERT member2
        em.persist(team1);          // INSERT team1, UPDATE member1.fk, UPDATE member2.fk
    }

    /**
     * Table 생성 결과
     * INSERT INTO PRODUCT...
     * INSERT INTO MEMBER...
     * INSERT INTO MEMBER_PRODUCT...
     */
    private static void mtMSingleSave() {
        ProductSingle productA = new ProductSingle();
        productA.setId("productA");
        productA.setName("상품A");
        em.persist(productA);

        MemberMtMSingle member1 = new MemberMtMSingle();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.getProducts().add(productA);
        em.persist(member1);
    }

    private static void mtMSingleFind() {
        MemberMtMSingle member = em.find(MemberMtMSingle.class, "member1");
        List<ProductSingle> products = member.getProducts();
        for(ProductSingle product : products) {
            System.out.println("product.getName() = " + product.getName());
        }
    }

    private static void mtMBiSave() {
        ProductBi productA = new ProductBi();
        productA.setId("productA");
        productA.setName("상품A");
        em.persist(productA);

        MemberMtMBi member1 = new MemberMtMBi();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.getProducts().add(productA);
        em.persist(member1);
    }

    /**
     * 역방향 탐색
     */
    private static void mtMBiFind() {
        ProductBi product = em.find(ProductBi.class, "productA");
        product.getMembers().forEach(member -> {
            System.out.println("member.getUsername() = " + member.getUsername());
        });
    }


    /**
     * 복합키 기반 엔티티 저장 방식
     */
    private static void compositeSaveAndFind() {
        ProductComp productA = new ProductComp();
        productA.setId("productA");
        productA.setName("상품A");
        em.persist(productA);

        MemberComp member1 = new MemberComp();
        member1.setId("member1");
        member1.setUsername("회원1");
        em.persist(member1);

        MemberProduct memberProduct = new MemberProduct();
        memberProduct.setMember(member1);
        memberProduct.setProduct(productA);
        memberProduct.setOrderAmount(2);

        em.persist(memberProduct);

        // 기본 키 값 생성 후 조회
        MemberProductId memberProductId = new MemberProductId();
        memberProductId.setMember("member1");
        memberProductId.setProduct("productA");
        MemberProduct findMemberProduct = em.find(MemberProduct.class, memberProductId);
        MemberComp findMember = findMemberProduct.getMember();
        ProductComp findProduct = findMemberProduct.getProduct();

        System.out.println("findMember = " + findMember.getUsername());
        System.out.println("findProduct = " + findProduct.getName());
        System.out.println("findMemberProduct.getOrderAmount() = " + findMemberProduct.getOrderAmount());
    }

    /**
     * 새로운 기본 키 사용 전략
     */
    private static void newPrimarySaveAndFind() {
        Product productA = new Product();
        productA.setId("productA");
        productA.setName("상품A");
        em.persist(productA);

        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        em.persist(member1);

        Order order = new Order();
        order.setMember(member1);
        order.setProduct(productA);
        order.setOrderAmount(2);
        em.persist(order);

        long orderId = 1L;
        Order findOrder = em.find(Order.class, orderId);
        Member member = findOrder.getMember();
        Product product = findOrder.getProduct();

        System.out.println("member.getUsername() = " + member.getUsername());
        System.out.println("product.getName() = " + product.getName());
        System.out.println("order.getOrderAmount() = " + order.getOrderAmount());
    }
}
