package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.app.model.TodoTask;
import org.app.service.TodoTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SearchController {

    private final TodoTaskService todoService;

    @GetMapping("/search")
    public String searchTasks(Model model, String keyword)  {

        if (keyword != null) {
            List<TodoTask> tasksForSearch = todoService.findTasksByTitle(keyword);
            model.addAttribute("userBoards", tasksForSearch);
        } else {
            model.addAttribute("userBoards", todoService.getAllTasks());
        }
        return "home :: #boardList";
    }

}
