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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import static org.app.model.enums.Status.DONE;

@Slf4j
@Controller
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

        User user = userService.getCurrentUser();
        Board board = boardService.findBoardById(boardId);

        LocalDate convertedDeadline = LocalDate.parse(deadline);

        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline,
            Status.valueOf(board.getBoardName()));
        log.info("After creating TodoTask");
        todoTask.setUser(user);
        todoTask.setBoard(board);
        taskService.insertTask(todoTask);
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

    @PostMapping("/remove/{id}")
    public String removeTask(@PathVariable("id") Integer taskId) {
        try {
            User currentUser = userService.getCurrentUser();
            taskService.deleteTaskById(currentUser.getId(), taskId);
        } catch (TaskNotFoundException | UserNotFoundException exception) {
            //
        }
        return "redirect:/home";
    }


    @GetMapping("/filter/priority")
    public String getTaskByPriority(@RequestParam("priority") Priority priority,
                                            @RequestParam("userId") int userId,
                                            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.getUserById(userId);
            List<TodoTask> tasks = taskService.getTaskByPriority(priority);
            redirectAttributes.addFlashAttribute("tasks", tasks);
            redirectAttributes.addFlashAttribute("currentUser", currentUser);
            redirectAttributes.addFlashAttribute("selectedPriority", priority);
            return "redirect:/home";
        } catch (UserNotFoundException | TaskNotFoundException exception) {
//
        }
        return "redirect:/home";
    }
    @GetMapping("/filter/boardName")
    public String getTasksByBoardName(@RequestParam(required = false) String boardName,
                                      @RequestParam("userId") int userId,
                                      RedirectAttributes redirectAttributes){
        try{
            User currentUser = userService.getCurrentUser();
            List<Board> boards = boardService.findBoardsByUserId(currentUser.getId());
            if (boardName != null && !boardName.isEmpty()) {
                boards = boards.stream()
                        .filter(board -> board.getBoardName().equals(boardName))
                        .collect(Collectors.toList());
            }
            redirectAttributes.addFlashAttribute("userBoards", boards);
            redirectAttributes.addFlashAttribute("currentUser", currentUser);
            redirectAttributes.addFlashAttribute("selectedBoardName", boardName);
        }catch (UserNotFoundException exception){
            //
        }
        return "redirect:/home";
    }


    @GetMapping("/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline) {
        try {
            return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));
        } catch (TaskNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

   /* @GetMapping("/filter/completed")
    public String getTaskByStatus(Model model) throws UserNotFoundException {
        int completedTasks = 0;
        int totalTasks = 0;

        try {
            User currentUser = userService.getCurrentUser();
            List<TodoTask> allTasks = userService.getTasksByUserId(currentUser.getId());
            List<TodoTask> doneTasks = allTasks.stream()
                    .filter(task -> task.getStatus() == Status.DONE)
                    .toList();
            totalTasks = allTasks.size();
            completedTasks = doneTasks.size();

            log.info("Total Tasks: " + totalTasks);
            log.info("Completed Tasks: " + completedTasks);

        } catch (Exception exception) {
            log.error("An error occurred while calculating task completion percentage", exception);
        }

        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);

        return "home";
    }*/


    @GetMapping("/filter/user")
    public ResponseEntity<List<TodoTask>> getAllTasksByUserId(@RequestParam("userId") int userId) {
        try {
            return ResponseEntity.ok(userService.getTasksByUserId(userId));
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
