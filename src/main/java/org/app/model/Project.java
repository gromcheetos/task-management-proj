package org.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Integer projectId;
    private String projectName;
    private String description;

    @OneToMany
    private Set<User> teamMembers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Board> boards;

    @ManyToOne
    private User projectOwner;

    @OneToMany(mappedBy = "project")
    private List<JobPosition> jobPositions = new ArrayList<>();

    public Project(String projectName) {
        this.projectName = projectName;
        this.boards = new ArrayList<>();
        this.teamMembers = new HashSet<>();
    }

    public Project() {
        this.projectName = "NOT SET";
        this.teamMembers = new HashSet<>();
        this.boards = new ArrayList<>();
        this.projectOwner = new User();
    }

    protected void addBoard(Board board) {
        this.boards.add(board);
    }

    protected void addTeamMember(User user) {
        this.teamMembers.add(user);
    }

    protected void removeTeamMember(User user) {
        this.teamMembers.remove(user);
    }

    protected void removeBoard(Board board) {
        this.boards.remove(board);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project project)) {
            return false;
        }
        return projectId == project.projectId && Objects.equals(projectName, project.projectName)
            && Objects.equals(teamMembers, project.teamMembers) && Objects.equals(projectOwner,
            project.projectOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, teamMembers, projectOwner);
    }
}
