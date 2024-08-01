package org.app.controllers;

import java.util.List;
import org.app.model.TodoTask;
import org.app.model.enums.Status;
import org.app.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    @Autowired
    private TodoTaskService todoService;

    @GetMapping("/todos")
    public String getTodos(Model model, @RequestParam(required = false) List<Status> statuses) {
        List<TodoTask> todos = todoService.getTodosByStatuses(statuses);
        model.addAttribute("todos", todos);
        model.addAttribute("selectedStatuses", statuses);
        model.addAttribute("statuses", Status.values());
        return "todos";
    }
}