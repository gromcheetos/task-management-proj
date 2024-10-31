package org.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer projectId;
    private String projectName;

    @OneToMany
    private Set<User> teamMembers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Board> boards;

    @OneToOne
    private User projectOwner;


    public Project(String projectName) {
        this.projectName = projectName;
    }

    public Project() {
        this.projectName = "NOT SET";
        this.teamMembers = new HashSet<>();
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

    @Override
    public String toString() {
        return "Project{" +
            "projectId=" + projectId +
            ", projectName='" + projectName + '\'' +
            ", teamMembers=" + teamMembers +
            ", projectOwner=" + projectOwner +
            '}';
    }
}
