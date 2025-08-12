package org.app.controllers.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.JobPosition;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.dto.JobPositionDto;
import org.app.service.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class ProjectCommon {

    private final UserService userService;
    private final TodoTaskService taskService;
    private final ProjectService projectService;
    private final JobPositionService jobPositionService;
    private final BoardService boardService;

    public String getHomePageUtility(Model model, Project activeProject) throws UserNotFoundException, ProjectNotFoundException {
        User currentUser = userService.getCurrentUser();
        log.info("activeProject: {}", activeProject);
        log.info("currentUser: {}", currentUser);
        if (currentUser == null) {
            return "redirect:/login";
        } else if (currentUser.getProjects().isEmpty()) {
            log.info("The user has no projects. Redirecting to /project/show");
            return "redirect:/project/show";
        }
        else {
            List<Project> projects = currentUser.getProjects();
            log.info("Projects: {}", projects);
            List<Board> boards = projectService.getAllBoardsByProjectId(activeProject.getProjectId());
            int totalTasks = taskService.getTasksByUserId(currentUser.getId()).size();
            log.info(currentUser.getUsername() + " has " + totalTasks);
            int completedTasks = taskService.getCompletedTasksCount(currentUser.getId());
            log.info(currentUser.getUsername() + " competed " + completedTasks);
            Set<User> teamMembers = activeProject.getTeamMembers();
            int memberCnt = teamMembers.size();
            List<JobPosition> positions = jobPositionService.findJobPositionById(activeProject.getProjectId());
            model.addAttribute("positions", JobPositionDto.fromEntity(positions));
            model.addAttribute("activeProject", activeProject);
            model.addAttribute("teamMembers", teamMembers);
            model.addAttribute("memberCnt", memberCnt);
            model.addAttribute("userBoards", boards);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("totalTasks", totalTasks);
            model.addAttribute("completedTasks", completedTasks);
            model.addAttribute("allProjects", projects);
        }
        return "home";
    }

}
