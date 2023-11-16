package jpabook.oopquerylanguage.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserDTO {

    private String username;
    private int age;

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
