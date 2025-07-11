package org.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Member member;

}
