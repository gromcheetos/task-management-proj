package org.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int boardId;
    private String boardName;
    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoTask> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Board(int boardId, String boardName, String description) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.description = description;
    }

    public Board(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }
}
