package jpabook.highlevelmapping.varioustable;

import javax.persistence.*;

@Entity
@Table(name = "BOARD")
@SecondaryTable(                // BOARD_DETAIL 추가 매핑(BoardDetail 없어도 자동 생성)
        name = "BOARD_DETAIL",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "BOARD_DETAIL_ID")
)
public class Board {

    @Id @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;

    @Column(table = "BOARD_DETAIL")             // BOARD_DETAIL 테이블의 컬럼에 매핑, BOARD_DETAIL 테이블엔 content 컬럼만 생성됨
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
