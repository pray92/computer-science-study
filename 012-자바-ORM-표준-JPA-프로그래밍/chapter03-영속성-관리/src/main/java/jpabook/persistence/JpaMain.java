package jpabook.persistence;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    /**
     * EntityManager Factory(README - 엔티티 매니저 팩토리와 엔티티 매니저 참고)
     * EntityManager를 만드는 공장, 비용이 상당히 큶
     * 한개만 만들어서 애플리케이션 전체에서 공유하도록 설계됨 -> Thread-safe
     * 모든 JPA 구현체는 EntityManagerFactory 생성 시 ConnectionPool도 만듦
     *  J2SE 환경에서 사용하는 방법
     *  J2EE 환경(스프링 프레임워크 포함)에서 사용 시 해당 컨테이너가 제공하는 데이터소스를 사용
     */
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    /**
     * EntityManager
     * 생성하는 비용은 크지 않음
     * 대신 thread-safe하지 않아 여러 Thread 접근 시 동시성 문제 발생 -> 절대 공유하면 안됨
     * EntityManager는 DB 연결이 꼭 필요한 시점까지 Connection을 얻지 않음
     * 예를 들면 Transaction이 시작할 때 DB Connection을 획득함
     */
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {
        // Transaction - 관리
        EntityTransaction tx = em.getTransaction(); // Transaction API
        try {
            // JPA 사용 시 반드시 Transaction 안에서 데이터 변경해야 함, 없이하면 예외 발생
            tx.begin();         // Transaction - 시작
            merge();            // Logic
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

    /**
     * 영속성 컨텍스트(Persistence Context)
     * 엔티티를 영구적으로 저장하는 '환경'
     * EntityManager로 Entity를 저장하거나 조회 시 EntityManager는 영속성 컨텍스트에 Entity를 보관하고 관리
     *
     * Entity 생명주기(README - Entity 생명주기)
     * 비영속(new/transient): 영속성 컨텍스트와 관계 없는 상태
     * 영속(managed): 영속성 컨텍스트에 저장된 상태
     * 준영속(detached): 영속성 컨텍스트에 저장되었다 분리된 상태
     * 삭제(removed): 삭제된 상태
     *
     * 영속성 컨텍스트 특징
     * 1. 영속성 컨텍스트와 식별자 값
     * 엔티티를 식별자 값(@Id로 테이블 기본 키와 매핑한 값)으로 구분
     * 따라서 영속 상태는 식별자 값이 반드시 있어야 함, 없으면 예외 발생
     * 2. 영속성 컨텍스트와 DB 저장
     * 영속성 컨텍스트에 엔티티 저장하면, JPA는 보통 Transaction을 커밋하는 순간 영속성 컨텍스트에 새로 저장된 엔티티를 DB에 반영 => flush
     */
    private static void lifeCycle() {
        // 비영속:
        // 순수 객체 상태이며 아직 저장하지 않음
        Member member = new Member();
        member.setId("member1");
        member.setUsername("user1");

        // 영속:
        // EntityManager를 통해 영속성 컨텍스테 저장된 상태
        em.persist(member);

        // 준영속
        // 영속성 컨텍스트에서 했다가 관리하지 않는 상태
        em.detach(member);

        // 삭제
        // 영속성 컨텍스트와 DB에서 삭제
        em.detach(member);
    }

    /**
     * 엔티티 조회
     *
     * 영속성 컨텍스트 장점
     * 1. **1차 캐시**
     * 2. **동일성 보장**
     * 3. Transaction을 지원하는 쓰기 지연
     * 4. 변경 감지
     * 5. 지연 로딩
     *
     * 영속성 컨텍스트는 내부에 캐시를 가짐 -> 1차 캐시
     * 영속 상태의 Entity는 모두 이곳에 저장
     *  Map이 있는데, @Id 매핑 필드가 key, 엔티티가 value
     */
    private static void inquireEntity() {
        // Entity 초기화
        Member member = new Member();
        member.setId("member1");
        member.setUsername("user1");

        // 엔티티 영속 상태, 1차 캐시에 저장
        em.persist(member);

        // 조회 시 1차 캐시에 있다면 이를 가져옴
        Member findMember = em.find(Member.class, "member1");

        // 조회 시 1차 캐시에 없으면 DB에서 조회해서 가져오고, 영속 상태의 엔티티를 1차 캐시에 등록하고 이를 반환
        Member findMember2 = em.find(Member.class, "not-in-cache-entity");

        // 여기서 식별자가 같은 엔티티 인스턴스를 조회 시 동일한 인스턴스를 반환 -> Entity 동일성 보장
        Member a = em.find(Member.class, "member1");
        Member b = em.find(Member.class, "member1");
        System.out.println(a == b); // true
    }

    /**
     * 엔티티 등록
     *
     * 영속성 컨텍스트 장점
     * 1. 1차 캐시
     * 2. 동일성 보장
     * 3. **Transaction을 지원하는 쓰기 지연**
     * 4. 변경 감지
     * 5. 지연 로딩
     *
     * 영속성 컨텍스트는 내부에 캐시를 가짐 -> 1차 캐시
     * 영속 상태의 Entity는 모두 이곳에 저장
     *  Map이 있는데, @Id 매핑 필드가 key, 엔티티가 value
     */
    private static void enrollEntity() {

        Member memberA = new Member();
        memberA.setId("member1");
        memberA.setUsername("user1");

        Member memberB = new Member();
        memberB.setId("member2");
        memberB.setUsername("user2");

        EntityTransaction tx = em.getTransaction();

        // EntityManager는 데이터 변경 시 Transaction을 시작해야 함
        tx.begin();

        em.persist(memberA);
        em.persist(memberB);
        // 이 때까지 INSERT SQL 문을 날리지 않음

        // Commit하는 순간 DB에 INSERT SQL문을 보냄
        // Entity Manager는 Commit 직전까지 내부 쿼리 저장소에 INSERT SQL을 모아둠
        // Commit하면 영속성 컨텍스트를 flush(), 모아둔 쿼리를 DB에 보냄 => Transaction을 지원하는 쓰기 지연(Write-behind)
        // Transaction Commit하지 않으면 DB에 저장되지 않음
        tx.commit();
    }


    /**
     * 엔티티 수정
     *
     * 영속성 컨텍스트 장점
     * 1. 1차 캐시
     * 2. 동일성 보장
     * 3. Transaction을 지원하는 쓰기 지연
     * 4. **변경 감지**
     * 5. 지연 로딩
     *
     * Entity는 데이터만 일부 변경해도 DB에 반영이 가능 -> 다양한 UPDATE SQL를 작성할 필요가 없음
     *
     * Dirty Checking: 엔티티 변경사항을 자동 반영하는 기능
     *
     * 작동 방식
     *  1. Transaction Commit 시 EntityManager 내부에서 먼저 flush() 호출
     *  2. Entity와 스냅샷 비교해서 변경된 Entity를 찾음
     *      *스냅샷: Entity를 영속성 컨텍스트에 보관 시 최초 상태를 복사해서 저장한 것
     *  3. 변경된 Entity가 있으면 수정 쿼리를 생성해서 쓰기 지연 SQL 저장소에 보냄
     *  4. 쓰기 지연 저장소의 SQL을 DB에 보냄
     *  5. DB Transaction Commit
     *
     * 변경 감지는 영속성 컨텍스트가 관리하는 영속 상태의 Entity에만 적용됨, 그 외는 DB에 반영되지 않음
     *
     * 변경 감지로 인한 UPDATE
     * 변경된 데이터 뿐만 아니라 동일한 데이터를 포함한 UPDATE SQL을 보냄
     * 이로 인해 DB에 데이터 전송량이 많아짐, 하지만 다음과 같은 이유로 모든 필드를 UPDATE
     *  1. 수정 쿼리가 일관적, 따라서 애플리케이션 로딩 시점에 미리 수정 쿼리를 생성해두고 재사용 가능
     *  2. DB에 동일한 쿼리를 보내면 DB는 이전에 한번 파싱된 쿼리를 재사용
     * 물론 필드가 너무 많거나 저장 내용이 너무 클 경우 동적으로 UPDATE SQL 생성하는 전략 존재 => Hibernate 확장 기능
     * ex.
     *     @org.hibernate.annotations.DynamicUpdate
     *     public class Member{ ... }
     *
     */
    private static void udpateEntity() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 영속 엔티티 조회
        Member member1 = em.find(Member.class, "member1");

        // 영속 엔티티 데이터 수정
        member1.setUsername("hi");
        member1.setAge(10);

        // 엔티티 삭제, 즉시 삭제가 아닌 등록처럼 동작해 삭제 쿼리를 쓰기 지연 SQL 저장소에 등록
        // 참고로 해당 엔티티는 영속성 컨텍스트에서 제거되므로, 재사용하지 않고 GC 처리되도록 두는 것이 좋음
        em.remove(member1);

        tx.commit();
    }

    /**
     * 플러시
     * flush()는 영속성 컨텍스트의 변경 내용을 DB에 반영
     *  1. 변경 감지 동작, 영속성 컨텍스트에 있는 모든 엔티티를 스냅샷과 비교해 수정된 엔티티를 찾음
     *     찾은 걸 기반으로 UPDATE SQL를 쓰기 지연 SQL 저장소에 저장
     *  2. 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송 -> 등록, 수정, 삭제 쿼리
     *
     * 영속성 컨텍스트 방법
     *  1. EnityManager.flush() 호출
     *     직접 호출해서 영속성 컨텍스트를 강제로 flush
     *     테스트 및 다른 프레임워크와 JPA 혼용할 때 빼곤 거의 사용안함
     *  2. Transaction.commit() 호출 시 자동 호출
     *     변경 내용을 SQL 전달하지 않고 Transaction만 Commmit 시 DB에 반영되지 않음
     *     그래서 Commit 전에 flush()가 호출해서 영속성 컨텍스트의 변경 내용을 DB에 반영해야 함
     *     JPA에선 이를 예방하기 위해 commit() 수행 시 flush()를 자동 호출
     *  3. JPQL 쿼리 실행 시 자동 호출
     *     JPQL이나 Criteria 같은 객체지향 쿼리 호출 시에 바로 flush() 호출
     *
     *  Flush 모드 옵션
     *  EntityManager에 모드 직접 지정 가능 -> javax.persistence.FlushModeType
     *     FlushModeType.AUTO: 커밋이나 쿼리 실행 시 플러시(Default)
     *     FlushModeType.COMMIT: 커밋할 때만 플러시, 최적화를 위해 사용됨
     *
     *  WARNING: flush()는 영속성 컨텍스트에 보관된 엔티티를 지우는 것이 아님 -> 변경된 내용을 DB에 동기화하는 것이 flush()
     */
    private static void flush() {
        Member member1 = em.find(Member.class, "member1");
        Member member2 = em.find(Member.class, "member1");
        Member member3 = em.find(Member.class, "member1");

        // 3. JPQL 쿼리 실행 시 자동 호출

        /*
            em.persist()로  member 3개를 영속 상태로 만듦
            아직 DB에 반영되지 않은 상태
         */
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        /*
            중간에 JPQL 실행
            이때 위에 있는 영속 상태 엔티티들이 아직 DB에 없어 쿼리 결과로 조회안됨
            이를 위해 JPQL은 쿼리 실행 전에 flush()를 통해 이를 예방

            INFO: 식별자를 기준으로 조회하는 find() 메서드 호출 시 flush() 실행되지 않음
        */
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> members = query.getResultList();
    }

    /**
     * 준영속(detached)
     * 영속 상태 엔티티가 영속성 컨텍스트에서 분리된 상태
     * 영속성 컨텍스트가 제공하는 기능을 사용할 수 없음
     *
     * 만드는 방법
     *  1. em.detach(entity): 특정 엔티티만 준영속 상태 전환
     *  2. em.clear(): 영속성 컨텍스트 완전히 초기화
     *  3. em.close(): 영속성 컨텍스트 종료
     *
     * 준영속 상태 특징
     *  1. 비영속 상태에 가까움: 1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩을 포함한 모든 기능도 동작 안함
     *  2. 식별자 값을 가짐: 비영속 상태는 식별자가 없을 수 있으나, 준영속 상태는 이미 한번 영속되어 반드시 식별자 값을 가짐
     *  3. 지연 로딩 불가:
     *      실제 객체 대신 프록시 객체 로딩하고, 사용할 때 영속성 컨텍스트를 통해 데이터를 불러옴
     *      준영속 상태는 영속성 컨텍스트가 관리하지 않으므로 지연 로딩 시 문제가 발생함
     */
    private static void detached() {
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        // 엔티티 생성, 비영속 상태
        Member memberA = new Member();
        memberA.setId("member1");
        memberA.setUsername("user1");

        // 회원 엔티티 영속 상태
        em.persist(memberA);

        // 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
        em.detach(memberA);

        // 커밋, 1차 캐시부터 쓰기 지연 SQL 저장소까지 해당 엔티티 관리 정보가 모두 제거됨
        tx.commit();
    }

    /**
     * 영속성 컨텍스트 초기화
     */
    private static void clear() {
        // 영속 상태 엔티티
        Member member1 = em.find(Member.class, "member1");

        // 영속성 컨텍스트 초기화
        em.clear();

        // 준영속 상태, 영속성 컨텍스트가 지원하는 변경 감지는 동작하지 않음
        member1.setUsername("changedName");
    }

    /**
     * 영속성 컨텍스트 종료
     * 종료 시 해당 영속성 컨텍스트가 관리하던 영속 상태의 엔티티가 모두 준영속 상태됨
     */
    private static void close() {
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Member memberA = em.find(Member.class, "memberA");
        Member memberB = em.find(Member.class, "memberB");

        tx.commit();

        // 커밋되어 DB에 반영되고, close로 인해 모든 엔티티가 준영속 상태가 됨
        em.close();
    }

    /**
     * 병합
     * 준영속 상태 엔티티를 다시 영속 상태로 변경하려면 병합을 사용
     * merge()는 준영속 상태 엔티티를 받아 그 정보로 '새로운' 상속된 엔티티를 반환
     */
    private static void merge() {
        Member member = createMember("memberA", "회원1");
        member.setUsername("회원명 변경");

        mergeMember(member);
    }

    private static Member createMember(String id, String username) {
        // 영속성 컨텍스트1 시작
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction tx1 = em1.getTransaction();
        tx1.begin();

        Member member = new Member();
        member.setId(id);
        member.setUsername(username);
        member.setAge(10);

        em1.persist(member);
        tx1.commit();

        // 영속성 컨텍스트1 종료
        // member 엔티티는 준영속 상태가 됨
        em1.close();

        return member;
    }

    private static void mergeMember(Member member) {
        // 영속성 컨텍스트2 시작
        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();

        tx2.begin();

        // 비영속 상태 엔티티를 merge해도 영속 상태로 전환 가능
        // 병합은 파라미터로 넘어온 엔티티의 식별자 값으로 영속성 컨텍스트 조회
        // 찾는 엔티티가 없으면 DB에서 조회
        // DB에서도 발견 못하면 새로운 엔티티를 생성해서 병합 -> merge는 준영속, 비영속을 신경쓰지 않음

        /*Member transientMember = new Member();
        transientMember.setId("transient");
        transientMember.setUsername("hi!");
        transientMember.setAge(9999);
        Member mergeMember = em2.merge(transientMember);*/

        Member mergeMember = em2.merge(member);
        tx2.commit();

        // 준영속 상태 -> 수정사항 반영 불가
        System.out.println("member = " + member.getUsername());

        // 영속 상태 -> 수정사항 반영
        System.out.println("mergeMember = " + mergeMember.getUsername());

        // 준영속 상태 -> 포함하지 않음
        System.out.println("em2 contains member = " + em2.contains(member));
        // 영속 상태 -> 포함함, 새로운 영속 상태 엔티티 반환
        System.out.println("em2 contains mergeMember = " + em2.contains(mergeMember));

        em2.close();
    }
}
