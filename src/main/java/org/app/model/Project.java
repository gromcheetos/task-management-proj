package org.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Project {

    @Id
    private int projectId;
    private String projectName;

    @OneToMany
    private Set<User> teamMembers;

    @OneToOne
    private User projectOwner;

    public Project() {
        this.projectName = "NOT SET";
        this.teamMembers = new HashSet<>();
        this.projectOwner = new User();
    }

    public Project(String projectName, User projectOwner) {
        this.projectName = projectName;
        this.teamMembers = new HashSet<>();
        this.projectOwner = projectOwner;
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
