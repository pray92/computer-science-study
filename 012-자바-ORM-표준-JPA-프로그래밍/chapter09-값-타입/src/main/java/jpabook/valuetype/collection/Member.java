package jpabook.valuetype.collection;

import javax.persistence.*;
import java.util.*;

@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(
            name = "FAVORITE_FOODS",
            joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "ADDRESS",
            joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    private List<Address> addressHistory = new ArrayList<>();

    protected Member() {
    }

    public Member(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Long getId() {
        return id;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

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

            tx = em.getTransaction();
            tx.begin();
            update();
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * - 값 타입 컬렉션을 사용하는 favoriteFoods, addressHistory에 @ElementCollection을 지정함
     *
     * - favoriteFoods는 기본 값 타입인 String을 컬렉션으로 가짐
     * - 이를 DB 테이블로 매핑해야 하는데 RDB의 테이블은 컬럼 안에 컬렉션을 포함할 수 없음
     * - 따라서 별도의 테이블을 추가하고 @CollectionTable을 사용해서 추가한 테이블을 매핑해야 함
     * - 그리고 favoriteFoods처럼 값으로 사용되는 컬럼이 하나면, @Column을 사용해 컬럼명을 지정할 수 있음
     *
     * - addressHisotry는 Embedded Type인 Address를 컬렉션으로 가짐
     * - 마찬가지로 별도의 테이블을 사용하며, 테이블 매핑정보는 @AttributeOverride를 사용해 재정의
     *
     * - 참고로 @CollectionTable 생략 시 기본 값을 사용해서 매핑(기본 값: {엔티티 이름}_{컬렉션 속성 이름}
     *  - ex. Member 엔티티의 addressHistory => Member_addressHistory
     */
    private static void insert() {
        Member member = new Member();

        // Embedded type
        member.setHomeAddress(new Address("통영", "뭉돌해수욕장", "660-123"));

        // 기본 값 타입 컬렉션
        member.getFavoriteFoods().add("짬뽕");
        member.getFavoriteFoods().add("짜장");
        member.getFavoriteFoods().add("탕수육");

        // Embedded type 컬렉션
        member.getAddressHistory().add(new Address("서울", "강남", "123-123"));
        member.getAddressHistory().add(new Address("서울", "강북", "000-000"));

        em.persist(member);
    }

    /**
     * - 값 타입 컬렉션은 영속성 전이`CASCADE` + 고아 객체 제거`ORPHAN REMOVE` 기능을 필수로 가진다고 볼 수 있음
     * - 또한 조회할 때 fetch 전략을 선택할 수 있음 -> `LAZY`가 기본
     */
    private static void query() {
        // SQL: SELECT * FROM MEMBER WHERE ID = 1
        Member member = em.find(Member.class, 1L);

        // member.homeAddress
        Address homeAddress = member.getHomeAddress();

        // member.favoriteFoods
        // SQL: SELECT MEMBER_ID, FOOD_NAME FROM FAVORITE_FOODS WHERE MEMBER_ID = 1
        Set<String> favoriteFoods = member.getFavoriteFoods();
        favoriteFoods.forEach(food -> System.out.println("food = " + food));

        // member.addressHistory
        // SQL: SELECT * FROM ADDRESS WHERE MEMBER_ID = 1
        List<Address> addressHistory = member.getAddressHistory();
        System.out.println("addressHistory.get(0) = " + addressHistory.get(0));
    }

    private static void update() {
        Member member = em.find(Member.class, 1L);

        // Embedded Type 수정 => UPDATE SQL
        member.setHomeAddress(new Address("새로운도시", "신도시1", "123456"));

        // 기본 값 타입 컬렉션 수정 => DELETE -> INSERT
        Set<String> favoriteFoods = member.getFavoriteFoods();
        favoriteFoods.remove("탕수육");
        favoriteFoods.add("치킨");

        // Embedded Type 컬렉션 수정 => DELETE -> INSERT
        // Address equals & hashCode 필수 구현
        List<Address> addressHistory = member.getAddressHistory();
        addressHistory.remove(new Address("서울", "강남", "123-123"));
        addressHistory.remove(new Address("새로운도시", "새로운 주소", "123-456"));
    }
}
