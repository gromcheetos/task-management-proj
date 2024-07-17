package org.app.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.TaskNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.app.service.BoardService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskRestController {

    private final TodoTaskService taskService;
    private final UserService userService;
    private final BoardService boardService;

    @PostMapping("/create") //endpoint
    public String createTask(@RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("priority") String priority,
        @RequestParam("deadline") String deadline,
        @RequestParam("status") String status,
        @RequestParam("userId") int userId,
        @RequestParam("boardId") int boardId) throws UserNotFoundException, BoardNotFoundException {

        log.info(
            "Creating task with title: {}, description: {}, priority: {}, deadline: {}, status: {}, userId: {}, boardId: {}",
            title, description, priority, deadline, status, userId, boardId);

        User user = userService.getCurrentUser();
        Board board = boardService.findBoardById(boardId);

        LocalDate convertedDeadline = LocalDate.parse(deadline);

        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline,
            Status.valueOf(board.getBoardName()));
        log.info("After creating TodoTask");
        todoTask.setUser(user);
        todoTask.setBoard(board);
        return "redirect:/home";
    }

    @PostMapping("/update/{taskId}")
    public ResponseEntity<TodoTask> updateTasks(@PathVariable("taskId") Integer taskId,
        @RequestBody TodoTask task) {
        log.info("Received RequestBody: {}", task);
        try {
            TodoTask todoTask = taskService.updateTask(taskId, task);
            return ResponseEntity.ok(todoTask);
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/")
    public ResponseEntity<TodoTask> getTaskById(@RequestParam("id") Integer taskId) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(taskId));
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeTask(@RequestParam("id") Integer taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<List<TodoTask>> getTaskByPriority(@RequestParam("priority") Priority priority) {
        try {
            return ResponseEntity.ok(taskService.getTaskByPriority(priority));
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline) {
        try {
            return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));
        } catch (TaskNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<TodoTask>> getTaskByStatus(@RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(taskService.getTaskByStatus(Status.valueOf(status)));
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
}
