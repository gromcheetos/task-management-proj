package org.app.controllers.util;

import lombok.AllArgsConstructor;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.service.ProjectService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ProjectCommon {

    private final UserService userService;
    private final TodoTaskService taskService;
    private final ProjectService projectService;

    public String getHomePageUtility(Model model, Project activeProject) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        } else if (currentUser.getProjects().isEmpty()) {
            return "redirect:/project/show";
        } else {
            List<Project> projects = projectService.getAllProjects();
            if (activeProject == null) {
                activeProject = projects.get(0);
            }
            List<Board> boards = activeProject.getBoards();
            int totalTasks = taskService.getTasksByUserId(currentUser.getId()).size();
            int completedTasks = taskService.getCompletedTasksCount(currentUser.getId());
            Set<User> teamMembers = activeProject.getTeamMembers();
            int memberCnt = teamMembers.size();
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
