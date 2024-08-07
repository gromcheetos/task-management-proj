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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/home")
public class HomePageController {

    private final BoardService boardService;
    private final UserService userService;
    private final TodoTaskService todoTaskService;

    @GetMapping
    public String getUserBoards(Model model, @RequestParam(required = false) List<Status> statuses)
        throws UserNotFoundException {

        User currentUser = userService.getCurrentUser();
        List<Board> boards;

        if (currentUser.getBoards().isEmpty()) {
            log.info("No boards found, loading a default board");
            boards = boardService.getAllDefaultBoards();
        } else {

            if (statuses != null) {
                boards = boardService.filterBoardTasksByStatuses(statuses, currentUser.getId());
            } else {
                boards = boardService.findBoardsByUserId(currentUser.getId());
            }
            log.info("Found {} boards", boards);
        }

        model.addAttribute("userBoards", boards);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("statuses", Status.values());

        return "home";
    }


}