package jpabook.oopquerylanguage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
/**
 * 기준점이 명확하다곤 할 수 없으나, 테스트 결과
 * 엔티티 클래스 아무곳에 선언해도 문제없이 동작하는 것으로 보임.
 *
 * 하나의 엔티티에 하나의 @SqlResultSetMapping만 사용가능하니,
 * 여러 매핑을 하나의 클래스에 선언하고 싶으면 @SqlResultSetMappings 사용
 */
@SqlResultSetMapping(name = "memberWithOrderCount",
        entities = {@EntityResult(entityClass = Member.class)},
        columns = {@ColumnResult(name = "ORDER_COUNT")}
)
/**
 * 다른 엔티티 클래스에 따로 존재해도 동작하는 것을 확인
 */
@NamedNativeQuery(
        name = "Member.memberWithOrderCount",
        query = "SELECT M.ID, AGE, USERNAME, TEAM_ID, I.ORDER_COUNT " +
                "FROM MEMBER M " +
                "LEFT JOIN " +
                "   (SELECT IM.ID, COUNT(*) AS ORDER_COUNT " +
                "   FROM ORDERS O, MEMBER IM " +
                "   WHERE O.MEMBER_ID = IM.ID) I " +
                "ON M.ID = I.ID",
        resultSetMapping = "memberWithOrderCount"

)
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Member() {
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Member{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
