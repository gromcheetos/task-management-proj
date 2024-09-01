package org.app.controllers.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.TaskNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.TodoTask;
import org.app.model.enums.Status;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskFilterController {

    private final TodoTaskService taskService;
    private final UserService userService;

    @GetMapping("/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline) {
        try {
            return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));
        } catch (TaskNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter/user")
    public ResponseEntity<List<TodoTask>> getAllTasksByUserId(@RequestParam("userId") int userId) {
        try {
            return ResponseEntity.ok(userService.getTasksByUserId(userId));
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public String getTodos(Model model, @RequestParam(required = false) List<Status> statuses) {
        List<TodoTask> todos = taskService.getTodosByStatuses(statuses);
        model.addAttribute("todos", todos);
        model.addAttribute("selectedStatuses", statuses);
        model.addAttribute("statuses", Status.values());
        return "todos";
    }


}
