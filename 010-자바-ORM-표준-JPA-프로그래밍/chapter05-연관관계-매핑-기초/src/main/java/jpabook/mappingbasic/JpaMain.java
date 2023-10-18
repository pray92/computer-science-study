package jpabook.mappingbasic;

import jpabook.mappingbasic.bidirection.MemberBi;
import jpabook.mappingbasic.bidirection.TeamBi;
import jpabook.mappingbasic.single.MemberSingle;
import jpabook.mappingbasic.single.TeamSingle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 연관관계 매핑 핵심 키워드
 *
 * 방향
 * - 단방향: 두 객체가 있을 때 한 쪽만 참조하는 경우
 * - 양방향: 두 객체가 있을 때 모두 서로 참조하는 경우
 *
 * 다중성
 * - 다대일 -> ex. 여러 회원은 하나의 팀에 속할 수 있다 => 회원 : 팀 = 다 : 1
 * - 일대다
 * - 일대일
 * - 다대다
 *
 * 연관관계 주인
 * - 객체를 양방향 연관관계로 만들면 주인을 정해야 함
 *
 * 객체와 테이블 간 연관관계 차이
 *
 * 객체:
 * - 기본적으로 단방향 연관관계이며, 양방향을 위해 따로 작업해줘야 함 -> 서로 다른 단방향 관계 2개
 * - 참조를 활용한 연관관계
 * class A {
 *     B b;     // 참조(주소)로 연관관계 맺음
 * }
 *
 * class B {
 *     A a;     // 양방향을 원하면 따로 참조 줘야 함
 * }
 *
 * 테이블:
 * - 기본적으로 외래키를 통한 JOIN으로 양방향 연관관계
 * - 외래 키를 활용한 연관관계
 * SELECT * FROM MEMBER M JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID(회원 -> 팀)
 * SELECT * FROM TEAM T JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID(회원 -> 팀)
 *
 */
public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            bidirection();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void insertSingle() {
        TeamSingle team1 = new TeamSingle();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        MemberSingle member1 = new MemberSingle();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setTeam(team1);
        em.persist(member1);

        MemberSingle member2 = new MemberSingle();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }

    /**
     * 객체 그래프 탐색 방식: 객체를 통해 연관된 엔티티 조회
     */
    private static void query() {
        insertSingle();

        MemberSingle member = em.find(MemberSingle.class, "member1");
        TeamSingle team = member.getTeam();
        System.out.println("team.getId() = " + team.getId());
        System.out.println("team.getName() = " + team.getName());
    }

    /**
     * 객체지향 쿼리 사용
     */
    private static void queryLogicJoin() {
        insertSingle();

        final String jpql = "select m from MemberSingle m join m.team t where t.name=:teamName";

        final List<MemberSingle> resultList = em.createQuery(jpql, MemberSingle.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        resultList.forEach(member -> {
            System.out.println("[query] member.username=" + member.getUsername());
        });
    }

    private static void update() {
        insertSingle();

        TeamSingle team2 = new TeamSingle();
        team2.setId("team2");
        team2.setName("팀2");
        em.persist(team2);

        MemberSingle member = em.find(MemberSingle.class, "member1");
        member.setTeam(team2);
    }

    private static void delete() {
        insertSingle();

        TeamSingle team1 = em.find(TeamSingle.class, "team1");

        MemberSingle member1 = em.find(MemberSingle.class, "member1");
        member1.setTeam(null);

        MemberSingle member2 = em.find(MemberSingle.class, "member2");
        member2.setTeam(null);

        // 팀에 대한 정보까지 완전히 삭제하려면
        // 연관관계를 모두 끊고 삭제해야 명
        member1.setTeam(null);
        member2.setTeam(null);
        em.remove(team1);
    }


    /**
     * WARNING: insertBi를 보면 setTeam을 통해 연관관계 주인인 Member에 연관관계를 맺는 방식을 시도했으나 동작 안함
     * 오히려 Team에서 members에 추가, 즉 연관관계 주인이 아닌 객체에 참조해야 매핑이 되는 것을 확인할 수 있음.
     * 레거시 버전 사용으로 인한 문제라 우선 추측, 책에 있는 내용대로 기술.
     */
    private static void insertBi() {

        TeamBi team1 = new TeamBi();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        MemberBi member1 = new MemberBi();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setTeam(team1);
        //team1.getMembers().add(member1);
        em.persist(member1);

        MemberBi member2 = new MemberBi();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setTeam(team1);
        //team1.getMembers().add(member2);
        em.persist(member2);
    }

    /**
     * 연관관계 주인:
     * 객체에는 양방향 연관관계라는 것이 없음 => 서로 다른 단방향 연관관계 2개를 로직으로 묶어서 양방향 처리한 것
     *  회원 -> 팀(단방향)
     *  팀 -> 회원(단방향)
     * 반면 테이블에는 외래 키 하나로 양쪽 테이블 JOIN 가능
     *  팀 <-> 회원(양방향)
     * 여기서, 엔티티를 양방향 연관관계 설정 시 객체 참조는 둘이고, 외래 키는 하나가 됨 => 둘 사이에 차이가 발생함
     * 이런 차이로 인해 JPA에선 두 객체 연관관계 중 하나를 정해서 테이블의 외래 키를 관리해야 하는 객체가 필요
     * 이것이 바로 '연관관계 주인'
     *
     * 양방향 매핑 규칙:
     * 연관관계 주인만이 DB 연관관계와 매핑되고, 외래 키를 관리(CUD)가 가능
     * 반면 주인 아닌 쪽은 읽기만 할 수 있음
     *
     * MemberBi & TeamBi 연관관계 매핑 시 이런식으로 된다'고' 함
     *  team.getMembers().add(member1);     // 무시(연관관계 주인 아님)
     *  team.getMembers().add(member1);     // 무시(연관관계 주인 아님)
     *  member1.setTeam(team);              // 연관관계 설정(주인)
     *  member1.setTeam(team);              // 연관관계 설정(주인)
     */
    private static void bidirection() {
        insertBi();

        final TeamBi team = em.find(TeamBi.class, "team1");
        System.out.println("team.getName() = " + team.getName());
        team.getMembers().forEach(member -> {
            System.out.println("member.getUsername() = " + member.getUsername());
        });
    }

    static class PureMember {
        private PureTeam team;

        public void setTeam(PureTeam team) {
            if (this.team != null) {
                this.team.getMembers().remove(this);
            }
            this.team = team;
            this.team.getMembers().add(this);
        }
    }

    static class PureTeam {
        List<PureMember> members = new ArrayList<>();

        public List<PureMember> getMembers() {
            return members;
        }
    }

    private static void pureObjects() {
        PureTeam team = new PureTeam();
        PureMember member1 = new PureMember();
        PureMember member2 = new PureMember();

        member1.setTeam(team);
        //team.getMembers().add(member1);
        member2.setTeam(team);
        //team.getMembers().add(member2);
    }
}
