package org.app.service;

import lombok.AllArgsConstructor;
import org.app.model.Board;
import org.app.model.Project;
import org.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Implement the ProjectService class
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectByName(String projectName){
        return projectRepository.findProjectByProjectName(projectName);
    }
    public List<Project> findProjectsByUserId(int userId){
        return projectRepository.findProjectsByUserId(userId);
    }
}
