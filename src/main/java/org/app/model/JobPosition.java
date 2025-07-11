package org.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPositionId;
    private String title;
    private String description;

    @ManyToOne
    private Project project;

    public JobPosition(String title) {
        this.title = title;
    }

}
