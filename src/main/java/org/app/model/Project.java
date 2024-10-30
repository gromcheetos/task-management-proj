package org.app.model;

import jakarta.persistence.Entity;
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
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Project {

    @Id
    private Integer projectId;
    private String projectName;

    @OneToMany
    private Set<User> teamMembers;

    @OneToMany
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

    public Project(String projectName, Set<User> teamMembers, List<Board> boards, User projectOwner) {
        this.projectName = projectName;
        this.teamMembers = new HashSet<>();
        this.boards = boards;
        this.projectOwner = projectOwner;
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
