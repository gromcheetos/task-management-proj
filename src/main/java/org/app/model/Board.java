package org.app.model;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

import java.util.List;
import org.app.model.enums.Status;

@Entity
@Data
@AllArgsConstructor
@Builder
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
        this.description = "This is a to_do board.";
        this.boardName = Status.TO_DO.getValue();
        this.status = Status.TO_DO;
    }

    public Board(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }


    public Board(String boardName, String description, boolean isDefault,
        Status status, List<TodoTask> tasks, User user) {
        this.boardName = boardName;
        this.description = description;
        this.isDefault = isDefault;
        this.status = status;
        this.tasks = tasks;
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board board)) {
            return false;
        }
        return boardId == board.boardId && isDefault == board.isDefault && Objects.equals(boardName,
            board.boardName) && Objects.equals(description, board.description) && status == board.status
            && Objects.equals(tasks, board.tasks) && Objects.equals(user, board.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, boardName, description, isDefault, status, tasks, user);
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
