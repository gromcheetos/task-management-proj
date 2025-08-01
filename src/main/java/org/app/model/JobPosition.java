package org.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"project", "user"})
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPositionId;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonManagedReference(value = "job-position-user")
    private User user;

    public JobPosition(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobPositionId, title, description);
    }
}
