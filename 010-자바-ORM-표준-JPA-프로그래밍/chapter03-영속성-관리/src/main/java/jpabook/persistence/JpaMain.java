package jpabook.persistence;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    /**
     * Since making factory cost is quite expensive, it is made only once so it shares everywhere in application.
     */
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    /**
     * It takes infinitesimal cost for creating manager using factory.
     *
     * Factory is thread-safe, whereas entity manager is not,
     * so manager must not be shared each thread.
     *
     * Entity Manager gets connection pools after starting transaction.
     *
     * Persistence Context:
     * Environment for persistently saving the entity
     * If entity is saved or read by manager, manager save and manage an entity in persistence context.
     * Features:
     *  1. PC distinguishes entity using identifier(Value mapped by {@code @Id})
     *      -> 'Managed' entity required identifier, if not, exception will be thrown
     *  2. JPA usually saves 'managed' entity in DB, called 'flush'
     *  3. It takes benefits if PC mangeds entities:
     *      1. 1-rd cache
     *      2. Consistence
     *      3. Transactional Write-behind(쓰기 지연)
     *      4. Dirty Check
     *      5. Lazy Loading
     *
     * Entity Lifecycle:
     * 1. new/transient(비영속): No relationship between entity and persistence context
     * 2. managed(영속): Saved in persistence context
     * 3. detached(준영속): Detached from persistence context
     * 4. removed(삭제): Removed
     */
    static EntityManager em = emf.createEntityManager();


	public static void main(String[] args) {
        mergeEntity();
    }

    /**
     * Persistence Context has internal cache called 1-rd cache(1차 캐시).
     *
     * All managed entity is saved in here.
     *
     * Easily say, PC has an internal map, key is identifier mapped by {@code @Id}, value is entity instance.
     */
    private static void inquireEntity() {
        // new/transient: entity is instantiated.
        Member member = new Member();
        member.setId("member1");
        member.setUsername("user1");

        // Entity is managed, saved in 1-rd cache
        em.persist(member);

        // Inquire from 1-rd cache
        Member findMember = em.find(Member.class, "member1");

        // If not in 1-rd cache, inquire from DB -> save in 1-rd cache -> managed entity is returned.
        Member findMember2 = em.find(Member.class, "not-in-cache-entity");
    }

    private static void ensureIdentity() {
        Member member = new Member();
        member.setId("member1");
        member.setUsername("user1");

        Member a = em.find(Member.class, "member1");
        Member b = em.find(Member.class, "member1");
        System.out.println("a == b: " + (a == b));
    }

    private static void enrollEntity() {

        Member memberA = new Member();
        memberA.setId("member1");
        memberA.setUsername("user1");

        Member memberB = new Member();
        memberB.setId("member2");
        memberB.setUsername("user2");

        EntityTransaction tx = em.getTransaction();

        // Entity manager should start transaction when trying data update.
        tx.begin();

        em.persist(memberA);
        em.persist(memberB);

        // Transaction doesn't send INSERT SQL util this block.

        // Send INSERT SQL after commit()
        // Entity manager gather INSERT SQL until transaction commit
        // It calls 'Transactional Write-behind(쓰기 지연)'.
        tx.commit();
    }

    private static void dirtyCheck() {
        EntityTransaction tx = em.getTransaction();
        // Inquire
        Member memberA = em.find(Member.class, "memberA");

        // Modification(Dirty)
        memberA.setUsername("hi");
        memberA.setAge(10);

        tx.begin();

        // No exists, manager provides automatic entity modification for DB -> 'Dirty Check'
        // JPA copies and saves the initial statement of entity before saving in PC, it called 'snapshot'
        // and find modified entity comparing with initial data and snapshot.
        //
        // Order)
        //      1. flush() is called when transaction commit
        //      2. Find modified data comparing with entity and snapshot
        //      3. If there is modified, send to transactional write-behind SQL storage creating UPDATE SQL
        //      4. Send TW-B SQL storage's SQL to DB
        //      5. Commit the DB Transaction
        //
        // Only managed entity may apply dirty check
        //em.update(memberA);

        tx.commit();
    }

    private static void removeEntity() {
        Member memberA = em.find(Member.class, "memberA");

        // It is not removed immediately.
        // DELETE SQL is enrolled in TW-B SQL storage, then send SQL after flush() is called -> DB delete the entity.
        // memberA is removed from PC, so it should not be reused, just let GC deal with it.
        em.remove(memberA);
    }

    /**
     * flush(): Let the modification(dirty) in persistence context send to DB.
     *
     * Order)
     *      1. Dirty check is started, find the modified entity in PC. Modified entity makes UPDATE SQL and write in
     *        transactional write-behind SQL storage
     *      2. Send the SQL in TW-B storage to DB
     *
     * There are 3 ways to flush in PC
     *      1. em.flush(): No used except for test or JPA with other framework
     *      2. Transaction.commit(): It includes flush() in commit()
     *      3. Automatically called when JPQL query called
     */
    private static void flushForEntityManager() {
        // Reason for automatic calling when JPQL query call.
        Member memberA = new Member();
        Member memberB = new Member();
        Member memberC = new Member();

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);

        // At this time, transaction not commit yet, JPQL inquires non-saved-in-DB data,
        // so PC must flush to DB before create query.
        // JPA calls flush() automatically for preventing this issue.
        //
        // NOTE: find(), inquiring based on @Id, does not call flush().
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> members = query.getResultList();
    }

    /**
     * 3 ways to entity to detach
     *      1. em.detach(entity): detach the specific entity
     *      2. em.clear(): clear all PC
     *      3. em.close(): close the PC
     *
     * Managed entity usually becomes detached when PC is closed.
     * It's not usual the developer handle by itself.
     *
     * Detached entity is,
     *      1. Same as transient: Not support 1rd-cache, transactional write-behind, dirty check, lazy loading.
     *      2. Having an identifier: It is managed once, so it must have identifier
     *      3. unable to lazy loading: Lazy loading is loading proxy object and loading data from PC when truly using.
     *                                 But, detached entity cannot be lazy loading for not in PC, it makes problems.
     */
    private static void detachEntity() {

        // new/transient
        Member memberA = new Member();
        memberA.setId("memberA");
        memberA.setUsername("hi");
        memberA.setAge(10);

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        // managed
        em.persist(memberA);

        // detached
        em.detach(memberA);

        tx.commit();
    }

    /**
     * For let detached entity change to managed state, we can use merge()
     * merge() takes detached entity then return 'newly managed' entity based on detached one.
     */
    private static void mergeEntity() {
        Member member = createMember("memberA", "회원1");
        member.setUsername("회원명변경");    // Modify in detached
        mergeMember(member);
    }

    private static Member createMember(String id, String username) {
        // First PC1 Start
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction tx1 = em1.getTransaction();
        tx1.begin();

        Member member = new Member();
        member.setId(id);
        member.setUsername(username);
        member.setAge(10);

        em1.persist(member);
        tx1.commit();

        // PC1 close, 'member' is now detached entity.
        em1.close();

        // First PC1 End

        return member;
    }

    private static void mergeMember(Member member) {
        // Second PC2 Start
        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();

        tx2.begin();

        // What if transient entity is merged? -> It also work!
        /*Member transientMember = new Member();
        transientMember.setId("transient");
        transientMember.setUsername("hi!");
        transientMember.setAge(9999);
        Member mergeMember = em2.merge(transientMember);*/

        Member mergeMember = em2.merge(member);
        tx2.commit();

        // Detached state
        System.out.println("member = " + member.getUsername());

        // Managed state
        System.out.println("mergeMember = " + mergeMember.getUsername());

        System.out.println("em2 contains member = " + em2.contains(member));
        System.out.println("em2 contains mergeMember = " + em2.contains(mergeMember));

        em2.close();
        // Second PC2 End
    }
}
