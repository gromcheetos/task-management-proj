package org.app.controllers.projects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.controllers.util.ProjectCommon;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Status;
import org.app.service.BoardService;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Set;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BoardService boardService;
    private final ProjectCommon projectCommon;
    
    @PostMapping("/create")
    public String createProject(@RequestParam("projectName") String projectName,
        @RequestParam(value = "description", required = false) String description,
        Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        Project project = new Project(projectName);
        project.setProjectOwner(currentUser);
        for (Status status : Status.values()) {
            Board board = boardService.createDefaultBoardsForNewProject(status.getValue());
            project.getBoards().add(board);
        }
        project.setDescription(description);
        projectService.createProject(project);
        return "redirect:/";
    }

    @PostMapping("/add/member")
    public String insertTeamMember(@RequestParam("projectId") int projectId,
        @RequestParam(value = "teamMembers", required = false) Set<User> teamMembers,
        Model model) throws UserNotFoundException, ProjectNotFoundException {
        User currentUser = userService.getCurrentUser();
        Project teamProject = projectService.findProjectByProjectId(projectId);
        teamProject.setTeamMembers(teamMembers);
        return "redirect:/";
    }

    @GetMapping("/show")
    public String showProjectPage(Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "create-project";
    }

    @GetMapping("/find/{projectId}")
    public String getProjectById(@PathVariable("projectId") int id, Model model)
        throws UserNotFoundException, ProjectNotFoundException {
        Project activeProject = projectService.findProjectByProjectId(id);
        return projectCommon.getHomePageUtility(model, activeProject);
    }
}
