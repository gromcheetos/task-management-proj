package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.User;
import org.app.model.dto.UserData;
import org.app.service.SearchService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;
    private final TodoTaskService taskService;

    @GetMapping("/search")
    public String searchTasks(Model model, String keyword) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        if (keyword != null) {
            List<Board> boards = searchService.performSearch(keyword);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("userBoards", boards);
            model.addAttribute("totalTasks", taskService.getTasksByUserId(currentUser.getId()).size());
            model.addAttribute("completedTasks", taskService.getCompletedTasksCount(currentUser.getId()));
        }

        return "home";
    }

    @GetMapping("/search/members")
    public  ResponseEntity<List<UserData>> searchMembers(@RequestParam(value = "keyword", required = false) String keyword) {

        List<UserData> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }
}
