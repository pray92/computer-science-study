package jpabook.oopquerylanguage.dto;

public class UserDTO {

    private final String username;
    private final int age;

    public UserDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
