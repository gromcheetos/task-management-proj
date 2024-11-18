package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Status;
import org.app.service.BoardService;
import org.app.service.ProjectService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/")
public class HomePageController {

    private final BoardService boardService;
    private final UserService userService;
    private final TodoTaskService taskService;
    private final ProjectService projectService;

    @GetMapping
    public String showHomePage(Model model, @RequestParam(required = false) List<Status> statuses)
        throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        } else if (currentUser.getProjects().isEmpty()) {
            return "redirect:/project/show";
        } else {
            List<Board> boards = boardService.findBoardsByUserId(currentUser.getId());
            List<Project> projects = projectService.getAllProjects();
            int totalTasks = taskService.getTasksByUserId(currentUser.getId()).size();
            int completedTasks = taskService.getCompletedTasksCount(currentUser.getId());
            model.addAttribute("userBoards", boards);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("totalTasks", totalTasks);
            model.addAttribute("completedTasks", completedTasks);
            model.addAttribute("allProjects", projects);
            return "home";
        }
    }

}