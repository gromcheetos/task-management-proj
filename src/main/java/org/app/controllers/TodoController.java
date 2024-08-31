package org.app.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.app.exceptions.TaskNotFoundException;
import org.app.model.TodoTask;
import org.app.model.enums.Status;
import org.app.service.TodoTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoTaskService todoService;

    @GetMapping("/todos")
    public String getTodos(Model model, @RequestParam(required = false) List<Status> statuses) {
        List<TodoTask> todos = todoService.getTodosByStatuses(statuses);
        model.addAttribute("todos", todos);
        model.addAttribute("selectedStatuses", statuses);
        model.addAttribute("statuses", Status.values());
        return "todos";
    }

    @GetMapping("/search")
    public String searchTasks(Model model, String keyword) throws TaskNotFoundException {

        if (keyword != null) {
            List<TodoTask> tasksForSearch = todoService.findTasksByTitle(keyword);
            model.addAttribute("userBoards", tasksForSearch);
        } else {
            model.addAttribute("userBoards", todoService.getAllTasks());
        }
        return "home :: #boardList";
    }
}