package org.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.*;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;

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
    private Status status;

    @ManyToOne
    @JoinColumn(name = "board_id")
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
    public TodoTask(Integer id, String title, String description, Priority priority, LocalDate deadline, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
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
