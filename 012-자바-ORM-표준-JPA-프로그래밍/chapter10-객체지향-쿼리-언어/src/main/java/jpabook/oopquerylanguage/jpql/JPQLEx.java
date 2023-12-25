package jpabook.oopquerylanguage.jpql;

import jpabook.oopquerylanguage.dto.UserDTO;
import jpabook.oopquerylanguage.entity.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

public class JPQLEx {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {


        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            insert();
            tx.commit();
            em.clear();

            query();
            parameterBasedOnName();
            parameterBasedOnPosition();
            variousValueQuery();
            join();
            fetchJoin();
            bulkCalc();
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
     * - 반환할 타입 지정 시 TypeQuery, 지정하지 않으면 Query 반환
     * - query.getResultList(): 결과를 예제로 반환, 결과가 없으면 빈 컬렉션 반환
     * - query.getSingleResult(): 결과가 정확히 하나일 떄 사용
     *  - 결과가 없으면 NoResultException 발생
     *  - 결과가 1개 초과 시 NonUniqueResultException 발생
     */
    private static void query() {
        //
        TypedQuery<Member> typedQuery = em.createQuery("SELECT m FROM Member m", Member.class);

        typedQuery.getResultList().forEach(member -> {
            System.out.println("member = " + member);
        });

        TypedQuery<String> wrgQuery = em.createQuery("SELECT username FROM Member", String.class);
        wrgQuery.getResultList().forEach(System.out::println);


        Query query = em.createQuery("SELECT username, age FROM Member");
        query.getResultList().forEach(o -> {
            Object[] result = (Object[]) o; // 결과가 둘 이상이면 Object[] 반환
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);
        });
    }

    /*
    * 이름 기준 파라미터: 파라미터를 이름으로 구분하는 방법, 앞에 : 사용
    * */
    private static void parameterBasedOnName() {
        String usernameParam = "member1";

        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m where m.username=:username", Member.class);
        query.setParameter("username", usernameParam);
        System.out.println("username = " + query.getSingleResult());
    }

    /**
     * 위치 기준 파라미터: 파라미터를 위치로 구분, 앞에 ? 사용하며 1-base
     */
    private static void parameterBasedOnPosition() {
        em.createQuery("SELECT m FROM Member m where m.username=?1", Member.class)
                    .setParameter(1, "member1")
                    .getResultList()
                .forEach(member -> System.out.println("member = " + member));
    }

    private static void variousValueQuery() {
        // 스칼라 타입과 엔티티 타입 여러 값을 함께 조회가 가능
        List<Object[]> resultList = em.createQuery("SELECT o.member, o.product, o.orderAmount FROM Order o").getResultList();
        resultList.forEach(result -> {
            Member member = (Member) result[0];
            System.out.println("member = " + member);
            Product product = (Product) result[1];
            System.out.println("product = " + product);
            Integer orderAmount = (Integer) result[2];
            System.out.println("orderAmount = " + orderAmount);
        });

        // 위 방식은 보통 사용하지 않고, DTO처럼 의미 있는 객체로 변환해서 사용함
        // NEW 명령어: 반환 받을 클래스를 지정할 수 있는데, 이 클래스의 생성자에 JPQL 조회 결과를 넘겨줄 수 있음
        // NEW 명령어 사용 주의사항
        //  1. 패키지 명을 포함한 전체 클래스 명 입력
        //  2. 순서와 타입이 일치하는 생성자가 필요
        TypedQuery<UserDTO> query = em.createQuery("SELECT new jpabook.oopquerylanguage.dto.UserDTO(m.username, m.age) FROM Member m", UserDTO.class);
        query.getResultList().forEach(dto -> System.out.println(dto));
    }

    private static void join() {
        String teamName = "teamA";

        // 내부 조인(INNER 생략 가능)
        // SQL 결과 - join team team1_ on member0_.team_id=team1_.id
        // 조금 다른 것을 확인할 수 있음
        // JPQL 조인은 연관 필드(m.team)를 사용함
        // JPQL 조인을 SQL 조인처럼 사용하면 문법 오류가 발생
        String query = "SELECT m FROM Member m INNER JOIN m.team t WHERE t.name = :teamName";
        //String query = "SELECT m FROM Member m INNER JOIN m.team t";                          // 잘못된 JPQL 조인이라는데 정상 수행
        List<Member> members = em.createQuery(query, Member.class)
                    .setParameter("teamName", teamName)
                .getResultList();
        System.out.println("Inner Join:");
        members.forEach(member -> System.out.println("member = " + member));

        // 외부 조인
        // SQL 외부 조인과 동일
        // OUTER 생략 가능해서 보통 LEFT JOIN으로 사용
        query = "SELECT m FROM Member m LEFT OUTER JOIN m.team t";
        members = em.createQuery(query, Member.class).getResultList();
        System.out.println("Outer Join:");
        members.forEach(member -> System.out.println("member = " + member));
    }

    private static void fetchJoin() {
        // 엔티티 페치 조인
        String jpql = "select m from Member m join fetch m.team";
        List<Member> members = em.createQuery(jpql, Member.class).getResultList();
        // 페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩 발생하지 않음
        // 이 덕에 프록시가 아닌 실제 엔티티가 초기화되고 Member가 준영속 상태가 되어도 연관된 Team 조회가 가능
        members.forEach(member -> System.out.println("member = " + member + "team = " + member.getTeam().getName()));

        // 컬렉션 페치 조인
        jpql = "select t from Team t join fetch t.members where t.name = 'teamA'";
        List<Team> teams = em.createQuery(jpql, Team.class).getResultList();
        teams.forEach(team -> {
            // team 객체의 주소를 보면 동일함 => 동일한 객체를 반환
            System.out.println("team.getName() = " + team.getName() + ", team = " + team);
            // 페치 조인으로 팀과 회원을 함꼐 조회해서 지연 로딩 발생하지 않음
            team.getMembers().forEach(member -> System.out.println("member = " + member));

        });
    }

    /**
     * 벌크 연산
     * 엔티티를 수정하려면 영속성 컨텍스으의 변경 감지 기능이나 병합을 사용, `삭제하려면 EntityManager.remove()`를 사용
     * 이 방법으로 수백 개 이상의 엔티티를 하나씩 수정 및 삭제하기엔 시간이 오래 걸림
     * 이 때 여러 건을 한번에 수정 및 삭제하는 '벌크 연산'을 사용하면 됨
     */
    private static void bulkCalc() {
        System.out.println("JPQLEx.bulkCalc");

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        // Update
        em.createQuery("update Product p " +
                        "set p.price = p.price * 1.1 " +
                        "where p.stockAmount < :stockAmount")
                .setParameter("stockAmount", 10)
                .executeUpdate();
        tx.commit();

        em.clear();

        System.out.println("Update");
        em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList()
                .forEach(product -> System.out.println("product = " + product));

        tx = em.getTransaction();
        tx.begin();
        // Delete
        // Order와 연관관계가 있어 cascade로 삭제해줘야 함
        // JPA의 cascade 방식이 동작하지 않아,
        // @OnDelete(action = OnDeleteAction.CASCADE) 사용(참고: Product.java)
        em.createQuery("delete Product p " +
                        "where p.price < :price")
                .setParameter("price", 3000)
                .executeUpdate();
        tx.commit();

        em.clear();

        System.out.println("Delete");
        em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList()
                .forEach(product -> System.out.println("product = " + product));
    }
}
