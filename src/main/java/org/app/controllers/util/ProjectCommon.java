package org.app.controllers.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.*;
import org.app.model.dto.JobPositionDto;
import org.app.service.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class ProjectCommon {

    private final UserService userService;
    private final TodoTaskService taskService;
    private final ProjectService projectService;
    private final JobPositionService jobPositionService;
    private final BoardService boardService;

    public String getHomePageUtility(Model model, Project currentProject) throws UserNotFoundException, ProjectNotFoundException, BoardNotFoundException {
        User currentUser = userService.getCurrentUser();
        log.info("currentProject: {}", currentProject);
        log.info("currentUser: {}", currentUser);
        if (currentUser == null) {
            return "redirect:/login";
        } else if (currentUser.getProjects().isEmpty() && currentUser.getOwnedProjects().isEmpty()) {
            log.info("The user has no projects. Redirecting to /project/show");
            return "redirect:/project/show";
        }else if (currentProject == null) {
            log.info("The user choose project. Redirecting to /project/select");
            return "redirect:/project/select";
        }
        else {
            List<Project> projects = currentUser.getProjects();
            projects.addAll(currentUser.getOwnedProjects());
            log.info("Projects: {}", projects);
            List<Board> boards = projectService.getAllBoardsByProjectId(currentProject.getProjectId());

            List<TodoTask> totalTasks = new ArrayList<>();
            for (Board board : boards) {
                List <TodoTask> userTasks = taskService.getTasksByBoardId(board.getBoardId());
                log.info("Tasks for board {}: {}", board.getBoardId(), userTasks);
                totalTasks.addAll(userTasks);
            }
            log.info("Total tasks: {}", totalTasks);
            int totalTasksCount = totalTasks.size();
            log.info(currentUser.getUsername() + " has " + totalTasksCount);
            int completedTasks = taskService.getCompletedTasksCount(totalTasks);
            log.info(currentUser.getUsername() + " completed " + completedTasks);

            Set<User> teamMembers = currentProject.getTeamMembers();
            log.info("Team members: {}", teamMembers);
            int memberCnt = teamMembers.size();
            List<JobPosition> positions = jobPositionService.findJobPositionById(currentProject.getProjectId());
            model.addAttribute("positions", JobPositionDto.fromEntity(positions));
            model.addAttribute("currentProject", currentProject);
            model.addAttribute("teamMembers", teamMembers);
            model.addAttribute("memberCnt", memberCnt);
            model.addAttribute("userBoards", boards);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("totalTasks", totalTasksCount);
            model.addAttribute("completedTasks", completedTasks);
            model.addAttribute("allProjects", projects);
        }
        return "home";
    }

}
