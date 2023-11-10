package jpabook.oopquerylanguage.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CaseDTO {

    private String username;

    private int price;

    public CaseDTO(String username, int price) {
        this.username = username;
        this.price = price;
    }

    @Override
    public String toString() {
        return "CaseDTO{" +
                "username='" + username + '\'' +
                ", price=" + price +
                '}';
    }
}
