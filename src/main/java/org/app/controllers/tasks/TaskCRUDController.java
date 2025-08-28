package org.app.controllers.tasks;

import lombok.RequiredArgsConstructor;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks")
public class TaskCRUDController {

    private final TodoTaskService taskService;
    private final UserService userService;
    private final BoardService boardService;

    @PostMapping("/create")
    public String createTask(@RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("priority") String priority,
        @RequestParam("deadline") String deadline,
        @RequestParam("userId") int userId,
        @RequestParam("boardId") int boardId,
        @RequestParam("status") String status) throws UserNotFoundException, BoardNotFoundException {
        log.info("Received status: {}", status);
        User user = userService.getCurrentUser();
        Board board = boardService.findBoardById(boardId);

        LocalDate convertedDeadline = LocalDate.parse(deadline);
        if("TO_DO".equals(status) || "IN_PROGRESS".equals(status) || "DONE".equals(status)){
            TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline);
            todoTask.setStatus(Status.valueOf(status));
            taskService.insertTask(todoTask);
            log.info("created task with status {}", status);
        }else{
            TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline);
            todoTask.setUser(user);
            todoTask.setBoard(board);
            taskService.insertTask(todoTask);
            log.info("After creating TodoTask");
        }
        return "redirect:/";
    }

    @PostMapping("/move")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> moveTask(
        @RequestParam("taskId") int taskId,
        @RequestParam("newBoardId") int boardId)
        throws TaskNotFoundException, BoardNotFoundException {
        log.info("Received taskId: {}", taskId);
        TodoTask todoTask = taskService.moveTask(taskId, boardId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task moved successfully");
        response.put("task", todoTask);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTask(
        @RequestParam("taskId") Integer taskId,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("priority") String priority,
        @RequestParam("deadline") String deadline)
            throws TaskNotFoundException {
        log.info("Received taskId: {}", taskId);
        LocalDate convertedDeadline = LocalDate.parse(deadline);
        TodoTask todoTask = taskService.getTaskById(taskId);
        todoTask.setTitle(title);
        todoTask.setDescription(description);
        todoTask.setPriority(Priority.valueOf(priority));
        todoTask.setDeadline(convertedDeadline);
        taskService.insertTask(todoTask);
        log.info("Updated task successfully");
        Map<String, Object> response = new HashMap<>();
        response.put("task", todoTask);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove/{id}")
    public String removeTask(@PathVariable("id") Integer taskId) {
        try {
            User currentUser = userService.getCurrentUser();
            taskService.deleteTaskById(currentUser.getId(), taskId);
        } catch (TaskNotFoundException | UserNotFoundException exception) {
            return "/error/error";
        }
        return "redirect:/";
    }

    @GetMapping("/detail/{taskId}")
    public ResponseEntity<TodoTask> getTaskDetail(@PathVariable Integer taskId) throws TaskNotFoundException {
        TodoTask task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
}
