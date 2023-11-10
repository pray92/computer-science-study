package jpabook.oopquerylanguage.criteria;

import jpabook.oopquerylanguage.dto.MemberTeamDTO;
import jpabook.oopquerylanguage.dto.UserDTO;
import jpabook.oopquerylanguage.entity.Member;
import jpabook.oopquerylanguage.entity.Order;
import jpabook.oopquerylanguage.entity.Product;
import jpabook.oopquerylanguage.entity.Team;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriteriaEx {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            insert();
            tx.commit();

            em.clear();

            tx = em.getTransaction();
            tx.begin();
            query();
            queryWithOrderBy();
            createQuery();
            select();
            group();
            having();
            sort();
            join();
            subquery();
            in();
            caseFunc();
            parameter();
            nativeFunc();
            dynamicJPQL();
            tx.commit();
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

        member1 = new Member("member1", 20);
        em.persist(member1);
    }

    private static void query() {
        System.out.println("CriteriaEx.query");

        // 쿼리 빌더
        // 쿼리를 생성하려면 먼저 빌더를 얻어야 함 -> EntityManager에서 얻을 수 있음
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Criteria 생성, 반환 타입 지정 가능
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);

        // FROM 절
        // 반환된 값 m은 Criteria에서 사용하는 별칭
        Root<Member> m = cq.from(Member.class);
        cq.select(m);       // SELECT 절

        TypedQuery<Member> query = em.createQuery(cq);
        query.getResultList().forEach(member -> System.out.println("member = " + member));
    }

    private static void queryWithOrderBy() {
        System.out.println("CriteriaEx.queryWithOrderBy");
        // SELECT m from Member m
        // where m.username = 'member1'
        // order by m.age desc

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);                     // FROM 절

        // 검색 조건 정의
        // m은 회원 엔티티의 별칭
        // cb.equal(A,B) => A=B
        Predicate usernameEqual = cb.equal(m.get("username"), "member1");

        // 정렬 조건 정의
        // JPQL의 m.age DESC
        javax.persistence.criteria.Order ageDesc = cb.desc(m.get("age"));

        // 쿼리 생성
        cq.select(m)
                .where(usernameEqual)
                .orderBy(ageDesc);

        em.createQuery(cq).getResultList().forEach(member -> System.out.println("member = " + member));
    }

    /**
     * Criteria 사용하려면 CriteriaBuilder.createQuery() 메서드로 쿼리를 생성하면 됨
     */
    private static void createQuery() {
        System.out.println("CriteriaEx.createQuery");

        // 1. 반환 타입 지정
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);
        cq.select(m);
        // 반환 타입을 지정했으므로, 바로 Member 타입 반환
        List<Member> members = em.createQuery(cq).getResultList();

        // 2. Object로 조회
        // 반환 타입을 지정할 수 없거나 반환 타입이 둘 이상이면 Object로 반환받으면 됨
        CriteriaQuery<Object> objectQuery = cb.createQuery();
        m = objectQuery.from(Member.class);     // 이때 정확한 타입 명시, 그렇지 않으면 IllegalStateException
        objectQuery.select(m);
        List<Object> resultList = em.createQuery(objectQuery).getResultList();

        // 3. Object[] 조회
        // 반환 타입이 둘 이상이면 Object[]를 사용하는 것도 방법
        CriteriaQuery<Object[]> objectArrQuery = cb.createQuery(Object[].class);
        m = objectArrQuery.from(Member.class);
        objectArrQuery.select(cb.array(m.get("username"), m.get("age")));
        List<Object[]> result = em.createQuery(objectArrQuery).getResultList();
    }

    private static void select() {
        System.out.println("CriteriaEx.select");

        // 1. 조회 대상을 한건 이상 지정
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> objectArrQuery = cb.createQuery(Object[].class);
        Root<Member> m = objectArrQuery.from(Member.class);
        objectArrQuery.select(cb.array(m.get("username"), m.get("age")));

        List<Object[]> result = em.createQuery(objectArrQuery).getResultList();
        // 2. DISTINCT
        // select, multiselect 다음에 distinct(true) 선언하면 됨
        objectArrQuery.multiselect(m.get("username"), m.get("age")).distinct(true);
        result = em.createQuery(objectArrQuery).getResultList();
        result.forEach(objects -> System.out.println(Arrays.toString(objects)));

        // 3. NEW, construct()
        // new 생성자() 구문을 cb.construct({클래스 타입}, ...)으로 사용
        CriteriaQuery<UserDTO> dtoQuery = cb.createQuery(UserDTO.class);
        m = dtoQuery.from(Member.class);

        dtoQuery.select(cb.construct(UserDTO.class, m.get("username"), m.get("age")));

        TypedQuery<UserDTO> dtoTypedQuery = em.createQuery(dtoQuery);
        List<UserDTO> dtoResult = dtoTypedQuery.getResultList();
        dtoResult.forEach(dto -> System.out.println("dto = " + dto));
        
        // 4. 튜플
        // Map과 비슷한 객체를 제공
        CriteriaQuery<Tuple> tupleQuery = cb.createTupleQuery();
        m = tupleQuery.from(Member.class);
        tupleQuery.multiselect(
                m.get("username").alias("username"),        // 튜플에서 사용할 튜플 결정
                m.get("age").alias("age")                   // 튜플은 튜플의 검색 키로 사용할 튜플 전용 별칭을 필수로 할당해야 함 -> alias()
        );

        TypedQuery<Tuple> tupleTypedQuery = em.createQuery(tupleQuery);
        List<Tuple> tupleList = tupleTypedQuery.getResultList();
        tupleList.forEach(tuple -> {
            // 선언해둔 튜플 별칭으로 조회 가능
            // 튜플은 이름 기반이라 순서 기반의 Object[] 보다 안전
            // tuple.getElements() 같은 메서드를 사용해 현재 튜플의 별칭과 자바 타입도 조회 가능
            // 튜플에 별칭을 준다해서 실제 SQL에 별칭이 달리는 것은 아님
            System.out.println("tuple.get(\"username\", String.class) = " + tuple.get("username", String.class));
            System.out.println("tuple.get(\"age\", Integer.class) = " + tuple.get("age", Integer.class));

        });
    }

    private static void group() {
        System.out.println("CriteriaEx.group");
        // GROUP BY
        // 팀 이름별로 나이가 가장 많은 사람과 가장 적은 사람 구하기
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member> m = cq.from(Member.class);

        Expression<Integer> maxAge = cb.max(m.<Integer>get("age"));
        Expression<Integer> minAge = cb.min(m.<Integer>get("age"));

        cq.multiselect(m.get("team").get("name"), maxAge, minAge);
        cq.groupBy(m.get("team").get("name"));      // GROUP BY

        TypedQuery<Object[]> query = em.createQuery(cq);
        query.getResultList().forEach(objects -> System.out.println(Arrays.toString(objects)));
    }

    private static void having() {
        System.out.println("CriteriaEx.having");
        // GROUP BY
        // 팀 이름별로 나이가 가장 많은 사람과 가장 적은 사람 구하기
        // 여기서 가장 나이 어린 사람이 10살을 초과하는 팀
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member> m = cq.from(Member.class);

        Expression<Integer> maxAge = cb.max(m.<Integer>get("age"));
        Expression<Integer> minAge = cb.min(m.<Integer>get("age"));

        cq.multiselect(m.get("team").get("name"), maxAge, minAge);
        cq.groupBy(m.get("team").get("name"))      // GROUP BY
                .having(cb.gt(minAge, 10));     // HAVING

        TypedQuery<Object[]> query = em.createQuery(cq);
        query.getResultList().forEach(objects -> System.out.println(Arrays.toString(objects)));
    }

    private static void sort() {
        System.out.println("CriteriaEx.sort");

        // 정렬 조건도 Criteria 빌더를 통해 생성
        // cb.desc(...), cb.asc(...)로 생성 가능
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);

        Predicate ageGt = cb.gt(m.<Integer>get("age"), 20);

        cq.select(m)
                .where(ageGt)
                .orderBy(cb.desc(m.get("age")));

        TypedQuery<Member> query = em.createQuery(cq);
        query.getResultList().forEach(member -> System.out.println("member = " + member));
    }

    private static void join() {
        System.out.println("CriteriaEx.join");
        // join() 메서드와 JoinType 클래스 사용
        // INNER: 내부 조인
        // LEFT: 왼쪽 외부 조인
        // RIGHT: 오른쪽 외부 조인, JPA 구현체나 DB에 따라 미지원할 수 있음
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MemberTeamDTO> cq = cb.createQuery(MemberTeamDTO.class);
        Root<Member> m = cq.from(Member.class);

        // 쿼리 루트(m)에서 바로 m.join("team") 메서드를 사용해 회원과 팀을 조인함
        // 그리고 조인한 team에 t라는 별칭 부여
        // 여기서 JoinType.INNER를 설정해서 내부 조인을 사용, 참고로 조인 타입을 생략하면 내부 조인을 사용
        Join<Member, Team> t = m.join("team", JoinType.INNER);

        // 페치 조인은 다음처럼 사용
        // m.fetch("team", JoinType.INNER); -> 조인대상, JoinType

        cq.multiselect(m, t)
                .where(cb.equal(t.get("name"), "teamA"));

        TypedQuery<MemberTeamDTO> query = em.createQuery(cq);
        query.getResultList().forEach(dto -> System.out.println("dto = " + dto));
    }

    private static void subquery() {
        System.out.println("CriteriaEx.subquery");
        // 간단한 서브 쿼리
        /**
         * select m
         * from Member m
         * where m < (select AVG(m2.age) from Member m2)
         */
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> mainQuery = cb.createQuery(Member.class);

        // 서브 쿼리 생성
        Subquery<Double> subQuery = mainQuery.subquery(Double.class);
        Root<Member> m2 = subQuery.from(Member.class);
        subQuery.select(cb.avg(m2.<Integer>get("age")));

        // 메인 쿼리 생성
        Root<Member> m = mainQuery.from(Member.class);

        mainQuery.select(m)
                .where(cb.ge(m.<Integer>get("age"), subQuery));

        // 상호 관련 서브 쿼리
        /**
         * select m from Member m
         * where exists (select t from m.team t where t.name='teamA')
         */
        // 메인 쿼리와 서브 쿼리 간에 서로 관련이 있는 경우
        // 서브 쿼리에서 메인 쿼리의 정보를 사용하려면 메인 쿼리에서 사용한 별칭을 얻어야 함
        // 서브 쿼리는 메인 쿼리의 Root나 Join을 통해 별칭을 받아 사용
        mainQuery = cb.createQuery(Member.class);
        m = mainQuery.from(Member.class);       // 한번 사용했으면 재초기화 필요

        // 서브 쿼리 생성
        Subquery<Team> subquery = mainQuery.subquery(Team.class);
        Root<Member> subM = subquery.correlate(m);// 메인 쿼리 별칭

        Join<Member, Team> t = subM.join("team");
        subquery.select(t)
                .where(cb.equal(t.get("name"), "teamA"));

        // 메인 쿼리 생성
        mainQuery.select(m)
                .where(cb.exists(subquery));

        em.createQuery(mainQuery).getResultList().forEach(member -> System.out.println("member = " + member));
    }

    private static void in() {
        System.out.println("CriteriaEx.in");
        /**
         * select m from Member m
         * where m.username in ("member1", "meember2")
         */

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);

        cq.select(m)
                .where(cb.in(m.get("username"))
                        .value("member1")
                        .value("member2"));

        em.createQuery(cq).getResultList().forEach(member -> System.out.println("member = " + member));
    }

    private static void caseFunc() {
        System.out.println("CriteriaEx.caseFunc");
        // CASE 식: selectCase(), when(), otherwise() 사용
        /**
         * select m.username,
         *      case when m.age>=60 then 600
         *      case when m.age>=15 then 500
         *           else 10000
         *      end
         *  from Member m
         */
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);

        cq.multiselect(
                m.get("username"),
                cb.selectCase()
                        .when(cb.ge(m.<Integer>get("age"), 60), 600)
                        .when(cb.ge(m.<Integer>get("age"), 15), 500)
                        .otherwise(1000)
        );

        em.createQuery(cq).getResultList().forEach(dto -> System.out.println("dto = " + dto));
    }

    private static void parameter() {
        System.out.println("CriteriaEx.parameter");
        // 파라미터 정의
        /**
         * select m from Member m
         * where m.username = :usernameParam
         */
        // :PARAM1 처럼 Criteria도 파라미터 정의 가능
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);

        cq.select(m)
                .where(cb.equal(m.get("username"), cb.parameter(String.class, "usernameParam")));

        em.createQuery(cq)
                .setParameter("usernameParam", "member1")
                .getResultList()
            .forEach(member -> System.out.println("member = " + member));
    }

    private static void nativeFunc() {
        System.out.println("CriteriaEx.nativeFunc");
        // 네이티브 함수 호출
        // cb.function(...)을 사용하면 됨
        // 하이버네이트 구현체는 방언에 사용자 정의 SQL 함수를 등록해야 호출 가능
        // (H2DialectImpl, persistence.xml 참고)
        // see: https://ocwokocw.tistory.com/168
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Member> m = cq.from(Member.class);

        Expression<Integer> func = cb.function("SUM", Integer.class, m.get("age"));
        cq.select(func);

        em.createQuery(cq).getResultList().forEach(sum -> System.out.println("sum = " + sum));
    }

    /**
     * 동적 쿼리: 다양한 검색 조건에 따라 실행 시점에 쿼리를 생성
     * JPQL로 구성 시, 단순 문자 더하기만으로도 버그가 발생
     * 또는 where와 and의 위치를 구성하는 것도 신경써야 함
     */
    private static void dynamicJPQL() {
        // JPQL 동적 쿼리
        Integer age = 10;
        String username = null;
        String teamName = "팀A";

        //JPQL 동적 쿼리 생성
        StringBuilder jpql = new StringBuilder("select m from Member m join m.team t ");
        List<String> criteria = new ArrayList<String>();

        if (age != null)
            criteria.add(" m.age = :age ");
        if (username != null)
            criteria.add(" m.username = :username ");
        if (teamName != null)
            criteria.add(" t.name = :teamName ");

        if (criteria.size() > 0 )
            jpql.append(" where ");

        for (int i = 0; i< criteria.size(); i++) {
            if(i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        TypedQuery<Member> query = em.createQuery(jpql.toString(), Member.class);
        if (age != null)
            query.setParameter("age", age);
        if (username != null)
            query.setParameter("username", username);
        if (teamName != null)
            query.setParameter("teamName", teamName);

        List<Member> resultList = query.getResultList();
    }

    /**
     * 동적 쿼리: 다양한 검색 조건에 따라 실행 시점에 쿼리를 생성
     * 최소한 공백이나 where, and 위치로 인해 에러가 발생하지 않음
     * 그럼에도 여전히 코드가 읽기 힘든 단점이 존재
     */
    private static void dynamicCriteria() {
        Integer age = 10;
        String username = null;
        String teamName = "팀A";

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);

        Root<Member> m = cq.from(Member.class);
        Join<Member, Team> t = m.join("team");

        List<Predicate> criteria = new ArrayList<Predicate>();
        if (age != null)
            criteria.add(cb.equal(m.<Integer>get("age"), cb.parameter(Integer.class, "age")));
        if (username != null)
            criteria.add(cb.equal(m.get("username"), cb.parameter(String.class, "username")));
        if (teamName != null)
            criteria.add(cb.equal(t.get("name"), cb.parameter(String.class, "teamName")));

        cq.where(cb.and(criteria.toArray(new Predicate[0])));

        TypedQuery<Member> query = em.createQuery(cq);
        if (age != null)
            query.setParameter("age", age);
        if (username != null)
            query.setParameter("username", username);
        if (teamName != null)
            query.setParameter("teamName", teamName);

        query.getResultList().forEach(member -> System.out.println("member = " + member));
    }
}
