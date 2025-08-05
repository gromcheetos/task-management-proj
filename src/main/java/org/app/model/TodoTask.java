package org.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;

import java.time.LocalDate;

@Entity
@Data // Lombok annotation to generate getters and setters
@AllArgsConstructor //Annotation to generate a constructor with all Arguments
@NoArgsConstructor //Annotation to generate a constructor with no arguments
@EqualsAndHashCode //Annotation to generate equals and hashCode methods
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    public TodoTask(String title, String description, Priority priority, LocalDate deadline, Status status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }
    public TodoTask(String title, String description, Priority priority, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
    }
    public int getBoardId() {
        return board != null ? board.getBoardId() : null;
    }

    @Override
    public String toString() {
        if (user == null) {
            return "task_id: " + id + ", title: " + title + ", description: " + description + ", priority: " + priority
                    + ", deadline: " + deadline + ", status: " + status;
        }
        return "task_id: " + id + ", title: " + title + ", description: " + description + "user_id: " + user.getId()
                + ", user_name: " + user.getName() + ", user_email: " + user.getEmail();
    }

}
