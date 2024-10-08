package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Status;
import org.app.service.BoardService;
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

    // TODO: check why statuses is not used
    @GetMapping
    public String getUserBoards(Model model, @RequestParam(required = false) List<Status> statuses)
        throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        List<Board> boards;
        int completedTasks = 0;
        int totalTasks = 0;
        if (currentUser.getBoards().isEmpty()) {
            boards = boardService.getAllDefaultBoards();
        } else {
            boards = boardService.findBoardsByUserId(currentUser.getId());
            totalTasks = taskService.getTasksByUserId(currentUser.getId()).size();
            completedTasks = taskService.getCompletedTasksCount(currentUser.getId());
        }
        model.addAttribute("userBoards", boards);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);
        return "home";
    }

}