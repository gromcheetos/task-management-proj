package org.app.controllers.tasks;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.*;
import org.app.model.*;
import org.app.model.dto.TaskData;
import org.app.model.enums.*;
import org.app.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam("status") String status,
        @RequestParam("userId") int userId,
        @RequestParam("boardId") int boardId) throws UserNotFoundException, BoardNotFoundException {

        User user = userService.getCurrentUser();
        Board board = boardService.findBoardById(boardId);

        LocalDate convertedDeadline = LocalDate.parse(deadline);

        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline,
            Status.valueOf(board.getBoardName()));
        log.info("After creating TodoTask");
        todoTask.setUser(user);
        todoTask.setBoard(board);
        taskService.insertTask(todoTask);
        return "redirect:/";
    }

    @PostMapping("/move")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> moveTask(
        @RequestParam("taskId") Integer taskId,
        @RequestParam("boardId") Integer boardId)
        throws TaskNotFoundException, BoardNotFoundException {

        TodoTask todoTask = taskService.moveTask(taskId, boardId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task moved successfully");
        response.put("task", todoTask);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTask(@RequestParam("boardName") String boardName,
        @RequestParam("taskId") int taskId,
        @RequestBody TaskData taskData)
        throws TaskNotFoundException, BoardNotFoundException {
        Status status = Status.valueOf(boardName);
        LocalDate convertedDeadline = LocalDate.parse(taskData.getDeadline());
        TodoTask todoTask = taskService.updateTask(taskId, taskData.getTitle(), taskData.getDescription(),
            Priority.valueOf(taskData.getPriority()), convertedDeadline, status,
            Status.valueOf(boardName).toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task moved successfully");
        response.put("task", todoTask);

        return ResponseEntity.ok(response);
    }


    // TODO: handle exceptions by redirecting to an error page
    @PostMapping("/remove/{id}")
    public String removeTask(@PathVariable("id") Integer taskId) {
        try {
            User currentUser = userService.getCurrentUser();
            taskService.deleteTaskById(currentUser.getId(), taskId);
        } catch (TaskNotFoundException | UserNotFoundException exception) {
            //
        }
        return "redirect:/";
    }

    @GetMapping("/detail/{taskId}")
    public ResponseEntity<TodoTask> getTaskDetail(@PathVariable Integer taskId) throws TaskNotFoundException {
        TodoTask task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
}
