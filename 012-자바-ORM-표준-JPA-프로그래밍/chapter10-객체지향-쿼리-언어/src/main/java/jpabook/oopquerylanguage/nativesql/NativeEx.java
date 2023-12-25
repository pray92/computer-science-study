package jpabook.oopquerylanguage.nativesql;

import jpabook.oopquerylanguage.entity.Member;
import jpabook.oopquerylanguage.entity.Order;
import jpabook.oopquerylanguage.entity.Product;
import jpabook.oopquerylanguage.entity.Team;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

public class NativeEx {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {


        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            insert();
            tx.commit();

            em.clear();

            queryByEntity();
            queryByValue();
            queryUsingResultMapping();
            queryUsingNamedNativeQuery();
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

        Order order = new Order(30, null, member1, product1);
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
     * 엔티티 조회
     */
    private static void queryByEntity() {
        System.out.println("NativeEx.queryByEntity");

        // SQL 정의
        String sql = "SELECT ID, AGE, USERNAME, TEAM_ID FROM MEMBER WHERE AGE > ?";

        // 중요한 점은 Native SQL로 SQL만 직접 사용할 뿐이고 나머지는 JPQL과 동일
        // 조회한 엔티티도 영속성 컨텍스트에서 관리됨
        // em.createNativeQuery 호출 시 TypeQuery가 아닌 Query 반환 -> JPA 1.0에서 API 규약이 정의되어서 그럼
        Query nativeQuery = em.createNativeQuery(sql, Member.class)
                // JPA는 공식적으로 Native SQL에서 이름 기반 파라미터 지원 안함 -> 위치 기반 파라미터만 지원
                // 하지만 하이버네이트는 Native SQL의 이름 기반 파라미터 사용 가능
                .setParameter(1, 20);
         nativeQuery.getResultList().forEach(member -> System.out.println("member = " + member));
    }


    /**
     * 값 조회
     */
    private static void queryByValue() {
        System.out.println("NativeEx.queryByValue");
        // SQL 정의
        String sql = "SELECT ID, AGE, USERNAME, TEAM_ID FROM MEMBER WHERE AGE > ?";

        // 값 조회 시 두 번째 파라미터 미사용
        // 스칼라 값들을 조회했을 뿐이라 영속성 컨텍스트가 관리하지 않음 -> JDBC 사용한 것과 유사
        Query nativeQuery = em.createNativeQuery(sql/*, Member.class*/).setParameter(1, 10);
        List<Object[]> resultList = nativeQuery.getResultList();
        resultList.forEach(objects -> {
            System.out.println("id = " + objects[0]);
            System.out.println("age = " + objects[1]);
            System.out.println("name = " + objects[2]);
            System.out.println("team_id = " + objects[3]);
        });
    }

    /**
     * 결과 매핑 사용
     * 엔티티와 스칼라 값을 함께 조회하는 것처럼 매핑이 복잡해지면 @SqlResultSetMaping을 정의해서 결과 매핑을 사용
     */
    private static void queryUsingResultMapping() {
        System.out.println("NativeEx.queryUsingResultMapping");

        String sql =
                "SELECT M.ID, AGE, USERNAME, TEAM_ID, I.ORDER_COUNT " +
                "FROM MEMBER M " +
                "LEFT JOIN " +
                "   (SELECT IM.ID, COUNT(*) AS ORDER_COUNT " +
                "   FROM ORDERS O, MEMBER IM " +
                "   WHERE O.MEMBER_ID = IM.ID) I " +
                "ON M.ID = I.ID";

        // 두 번째 파라미터에 결과 매핑 정보 정의
        // (Member 엔티티 참고)
        Query nativeQuery = em.createNativeQuery(sql, "memberWithOrderCount");

        List<Object[]> resultList = nativeQuery.getResultList();
        resultList.forEach(objects -> {
            Member member = (Member)objects[0];
            BigInteger orderCount =  (BigInteger)objects[1];

            System.out.println("member = " + member);
            System.out.println("orderCount = " + orderCount);
        });
    }

    private static void queryUsingNamedNativeQuery() {
        System.out.println("NativeEx.queryUsingNamedNativeQuery");
        List<Object[]>  resultList = em.createNamedQuery("Member.memberWithOrderCount").getResultList();
        resultList.forEach(objects -> {
            Member member = (Member)objects[0];
            BigInteger orderCount =  (BigInteger)objects[1];

            System.out.println("member = " + member);
            System.out.println("orderCount = " + orderCount);
        });
    }

}
