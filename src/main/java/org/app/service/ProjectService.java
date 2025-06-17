package org.app.service;

import lombok.AllArgsConstructor;
import org.app.exceptions.ProjectNotFoundException;
import org.app.model.Project;
import org.app.model.User;
import org.app.repository.ProjectRepository;
import org.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

// TODO: Implement the ProjectService class
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findProjectByProjectId(Integer projectId) throws ProjectNotFoundException {
        return projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("No Project Found with id: " + projectId + " "));
    }
    public Project getProjectByName(String projectName){
        return projectRepository.findProjectByProjectName(projectName);
    }

    public List<Project> findProjectsByUserId(int userId){
        User user = userRepository.findById(userId).orElseThrow();
        return projectRepository.findProjectByProjectOwner(user);
    }

    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }

    public Set<User> getTeamMembers(int projectId){
        Project project = projectRepository.findById(projectId).orElseThrow();
        return project.getTeamMembers();
    }
}
