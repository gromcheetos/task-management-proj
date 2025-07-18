package org.app.controllers;

import lombok.AllArgsConstructor;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.User;
import org.app.service.SearchService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ResponseBody
    @GetMapping("/search/users")
    public List<Map<String, Object>> searchUsers(@RequestParam("keyword") String keyword){
        List<User> userList = userService.getAllUsers();
        return userList.stream()
                .filter(user ->
                        (user.getName() != null && user.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (user.getEmail() != null && user.getEmail().toLowerCase().contains(keyword.toLowerCase())) ||
                                (user.getUsername() != null && user.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                )
                .map(user -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("userId", user.getId()); // or getUserId() or whatever your ID method is
                    m.put("username", user.getName());
                    return m;
                })
                .collect(Collectors.toList());
    }
}
