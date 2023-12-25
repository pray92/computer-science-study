package jpabook.highlevelmapping.onetoone;

import javax.persistence.*;

@Entity
public class BoardDetail {

    @Id
    private Long boardId;

    @MapsId     // 식별자가 단순히 컬럼 하나면 @MapsId를 사용하고 속성 값을 비워두면 됨
                // 이때 @MapsId는 @Id를 사용해서 식별자로 지정한 BoardDetail.boardId와 매핑됨
    @OneToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    private String content;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
