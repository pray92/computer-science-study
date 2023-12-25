package jpabook.highlevelmapping.composite.idclass.nonid;

import javax.persistence.*;

@Entity
public class Child {
    @Id
    private String id;

    @ManyToOne
    @JoinColumns({                                      // 부모 테이블의 기본 키 컬럼이 복합 키이므로 자식 테이블의 외래 키도 복합 키
                                                        // 따라서 외래 키 매핑 시 여러 컬럼을 매핑해야 함
                                                        // @JoinColumns을 사용해 각 외래 키 컬럼을 @JoinColumn으로 매핑
            @JoinColumn(
                    name = "PARENT_ID1",                // 지금처럼 name과 referencedColumnName 속성이 값이 같으면 ref 생략 가능
                                                        // 안되는디?
                    referencedColumnName = "PARENT_ID1"
            ),
            @JoinColumn(
                    name = "PARENT_ID2",
                    referencedColumnName = "PARENT_ID2"
            )
    })
    private Parent parent;
}
