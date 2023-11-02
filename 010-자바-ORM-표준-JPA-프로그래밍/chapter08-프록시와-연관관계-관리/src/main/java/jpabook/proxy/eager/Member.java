package jpabook.proxy.eager;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    private String id;

    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    protected Member() {
    }

    public Member(String username, Team team) {
        this.username = username;
        this.team = team;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            insert();
            tx.commit();

            em.clear();

            tx = em.getTransaction();
            tx.begin();
            query();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void insert() {
        Team team = new Team();
        team.setId("teamId");
        team.setName("team1");

        Member member = new Member();
        member.setId("memberId");
        member.setUsername("member1");
        member.setTeam(team);

        em.persist(team);
        em.persist(member);
    }

    /**
     * 생성해서 DB 등록 후 조회, 즉시 Team 엔티티 정보가 조회됨
     * - 이때 대부분의 JPA 구현체는 즉시 로딩을 최적화하기 위해 가능하면 조인 쿼리를 사용
     * - 여기선 회원과 팀을 조인해서 쿼리 한번으로 두 엔티티를 모두 조회
     * - 그리고 실행하면 알 수 있듯이, JPA가 내부 조인(INNER JOIN)이 아닌 외부 조인(LEFT OUTER JOIN)을 사용
     *  - 왜냐하면 회원 테이블의 TEAM_ID 외래 키는 NULL 값을 허용하기 때문
     *  - 따라서 팀에 소속되지 않은 회원이 있을 가능성이 있어, 이를 모두 조회하기 위해 외부 조인을 사용 -> 내부 조인 시 이를 찾을 수 없음
     *  - 내부 조인을 사용하도록 하려면 외래 키에 NOT NULL 제약 조건을 설정 필수 관계임을 명시하면 됨
     *      @JointColumn(nullalble = false) -> NULL 허용하지 않음, 내부 조인 사용
     *  - 혹은 다중성 관련 에너테이션에 optional = false를 주면 됨
     *      @ManyToOne(optional = false) -> 참고로 @~ToOne에만 optional 속성 존재
     *  - 정리하자면 JPA는 선택적 관계(nullable)이면 외부 조인을 사용, 필수 관계면 내부 조인을 사용
     */
    private static void query() {
        Member member = em.find(Member.class, "memberId");
        Team team = member.getTeam();
    }
}
