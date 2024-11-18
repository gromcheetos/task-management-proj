package org.app.controllers.projects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BoardService boardService;

    // TODO: add board to the project as well changed
    @PostMapping("/create")
    public String createProject(@RequestParam("projectName") String projectName,
        @RequestParam(value = "boardName", required = false) List<String> boardNames,
        @RequestParam(value = "teamMembers", required = false) Set<User> teamMembers,
        Model model) throws UserNotFoundException {

        User currentUser = userService.getCurrentUser();
        Project project = new Project(projectName);
        project.setProjectOwner(currentUser);
        projectService.createProject(project);
        Integer newProjectId = project.getProjectId();
        List<Board> boards = Collections.singletonList(
            boardService.createBoard(newProjectId, (Board) boardNames));
        project.setBoards(boards);
        project.setTeamMembers(teamMembers);
        return "redirect:/";
    }

    @GetMapping("/show")
    public String showProjectPage(Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "create-project";
    }
}
