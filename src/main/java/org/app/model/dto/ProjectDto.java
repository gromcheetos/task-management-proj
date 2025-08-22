package org.app.model.dto;

import lombok.Data;
import org.app.model.Project;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProjectDto {
    private int projectId;
    private String projectName;
    private List<JobPositionDto> positions;
    private Set<UserDto> teamMembers;

    public static ProjectDto fromEntity(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        if (project.getJobPositions() != null) {
            dto.setPositions(
                    project.getJobPositions().stream()
                            .map(JobPositionDto::fromEntity)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

}
