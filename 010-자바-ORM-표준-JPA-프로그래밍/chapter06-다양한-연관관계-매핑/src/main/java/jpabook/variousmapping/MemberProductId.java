package jpabook.variousmapping;

import java.io.Serializable;

public class MemberProductId implements Serializable {

    private Long member;  // MemberProduct.member와 연결
    private String product; // MemberProduct.product와 연결

    // hashCode and equals

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
