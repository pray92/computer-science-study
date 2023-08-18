package jpabook.entitymapping;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"}
)})
public class Member {

    @Id                         // Applicable -> primitive type, primitive wrapper type(String, java.util.Date, java.math.BigDecimal, etc)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 10)
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // The length of description of user is unlimited.
    // To write unlimited, it should use CLOB rather than VARCHAR
    // It's able to map with CLOB, BLOB types.
    // CLOB: String, char[], java.sql.CLOB
    // BLOB: byte[], java.sql.BLOB
    @Lob
    private String description;

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
