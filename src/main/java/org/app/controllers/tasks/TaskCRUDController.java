package org.app.controllers.tasks;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.*;
import org.app.model.*;
import org.app.model.enums.*;
import org.app.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/update")
    public ResponseEntity<TodoTask> updateTask(@RequestParam("taskId") Integer taskId,
        @RequestParam("newBoardId") Integer newBoardId) {

        try {
            TodoTask todoTask = taskService.updateTask(taskId, newBoardId);
            return ResponseEntity.ok(todoTask);
        } catch (TaskNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BoardNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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
    public String getTaskDetail(@PathVariable("taskId") int id, Model model) throws TaskNotFoundException {
        TodoTask task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "task-detail";
    }

}
