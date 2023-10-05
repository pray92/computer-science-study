package jpabook.variousmapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @ManyToOne                      // Member가 다에 속하므로, 연관관계 주인
                                    // 양방향인 경우에도 외래키가 있는 쪽이 연관관계 주인
                                    // 또한 양방향 연관관계는 항상 서로를 참조해야 함
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)    // 다대일 단방향 매핑을 읽기 전용으로 추가
                                                                            // 일대다 양방향처럼 보이게 하는 기법
    private Team team;
    private Integer age;

    @OneToOne(mappedBy = "member")
    private Locker locker;

/*    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT",                             // 연결 테이블 지정
            joinColumns = @JoinColumn(name = "MEMBER_ID"),          // 현 방향인 회원과 매핑할 조인 컬럼 정보 지정
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")   // 반대 방향인 상품과 매핑할 조인 컬럼 정보 지정
    )
    private List<Product> products = new ArrayList<>();*/

    // 역방향
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts;

    public Member() {
    }

    public Member(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;

        // 무한루프에 빠지지 않도록 체크
        if(!team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    /*public List<Product> getProducts() {
        return products;
    }*/
}
