package jpabook.variousmapping.manytoone;

import javax.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    // 없으면 단방향, 있으면 양방향
    @OneToOne(mappedBy = "locker")
    private MemberMtO member;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MemberMtO getMember() {
        return member;
    }

    public void setMember(MemberMtO member) {
        this.member = member;
    }
}
