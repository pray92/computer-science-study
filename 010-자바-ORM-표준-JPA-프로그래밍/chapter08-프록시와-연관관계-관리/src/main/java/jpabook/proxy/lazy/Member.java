package jpabook.proxy.lazy;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    private String id;

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
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

    /**
     * - em.find로 호출 시 회원만 조회하고 팀은 조회하지 않음
     * - 대신에 조회한 회원의 team 멤버변수에 프록시 객체를 넣어 둠
     * - 그리고 프록시 객체는 실제로 사용될 때까지 데이터 로딩을 미룸
     * - 그래서 실제 실행하면 select를 두번 사용하는 것을 확인할 수 있음(Memeber 한번, Team 한번)
     * - 참고로 조회 대상이 영속성 컨텍스트에 있으면 프록시 객체를 사용할 이유가 없으므로 이땐 실제 객체를 사용함
     */
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
    private static void query() {
        Member member = em.find(Member.class, "memberId");
        Team team = member.getTeam();
        team.getName();
    }
}
