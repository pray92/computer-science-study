package jpabook.persistence;

// JPA 에너테이션 패키지는 javax.persistence
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                     // 이 클래스를 테이블과 매핑한다고 JPA에게 알려줌, 이를 엔티티 클래스라 함
@Table(name = "MEMBER")     // 엔티티 클래스에 매핑할 테이블 정보를 알려줌
                            // 여기선 name 속성을 사용해 Member 엔티티를 MEMBER 테이블에 매핑함
public class Member {

    @Id                     // 테이블의 기본 키에 매핑, id 필드를 테이블의 ID 기본 키 컬럼에 매핑 -> 식별자 필드
    @Column(name = "ID")    // 필드를 컬럼에 매핑, 여기선 name 속성을 사용해서 Member 엔티티의 username 필드를 NAME 컬럼에 매핑
    private String id;

    @Column(name = "NAME")
    private String username;// 매핑 에너테이션이 없음, 이르면 필드 명을 사용해서 컬럼명으로 매핑함
                            // 필드 명이 age 이므로 age 컬럼으로 매핑

    private Integer age;    // 매핑 정보가 없는 필드
                            // 필드 명을 사용해서 컬럼 명으로 매핑

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
