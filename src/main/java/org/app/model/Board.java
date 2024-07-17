package org.app.model;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

import java.util.List;
import org.app.model.enums.Status;

@Entity
@Data
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int boardId;
    private String boardName;
    private String description;
    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoTask> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Board() {
        this.isDefault = true;
        this.description = "";
        this.boardName = Status.TO_DO.getValue();
    }

    public Board(int boardId, String boardName, String description) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.description = description;
    }

    public Board(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board board)) {
            return false;
        }
        return boardId == board.boardId && Objects.equals(boardName, board.boardName);
    }

    @Override
    public String toString() {
        return "Board{" +
            "boardId=" + boardId +
            ", boardName='" + boardName + '\'' +
            ", description='" + description + '\'' +
            ", isDefault=" + isDefault +
            ", status=" + status +
            ", tasks=" + tasks +
            '}';
    }
}
