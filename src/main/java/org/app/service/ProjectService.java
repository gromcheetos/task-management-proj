package org.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.ProjectNotFoundException;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.repository.ProjectRepository;
import org.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Project createOrUpdateProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findProjectByProjectId(Integer projectId) throws ProjectNotFoundException {
        log.info("[findProjectByProjectId] - Searching project by projectId:" + projectId);
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("No Project Found with id: " + projectId + " "));
    }

    public Project getProjectByName(String projectName) {
        return projectRepository.findProjectByProjectName(projectName);
    }

    public List<Project> findProjectsByUserId(int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return projectRepository.findProjectByProjectOwner(user);
    }

    public List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    public Set<User> getTeamMembers(int projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        return project.getTeamMembers();
    }

    public Project updateProject(int projectId, Board board) {
        Project updatedProj = projectRepository.findById(projectId).orElseThrow();
        List<Board> boards = updatedProj.getBoards();
        boards.add(board);
        return projectRepository.save(updatedProj);
    }

}
