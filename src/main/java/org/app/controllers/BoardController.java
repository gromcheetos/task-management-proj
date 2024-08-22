package org.app.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.ui.Model;


@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<Board> createBoard(@RequestParam("boardName") String boardName,
        @RequestParam("description") String description) {
        try {
            User currentUser = userService.getCurrentUser();
            Board board = new Board(boardName, description);
            board.setUser(currentUser);
            return ResponseEntity.ok(boardService.createBoard(board));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Board> updateBoard(
        @RequestParam("boardId") Integer boardId,
        @RequestParam("boardName") String boardName,
        @RequestParam("description") String description) {
        try {
            Board updateBoard = boardService.updateBoard(boardId, boardName, description);
            return ResponseEntity.ok(updateBoard);
        } catch (BoardNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   @GetMapping("/filter")
   public String boardsByNameAndPriority(@RequestParam(value = "boardName", required = false) List<String> boardNames,
                                         @RequestParam(value = "priority", required = false) List<String> priorities,
                                         @RequestParam("userId") Integer userId,
                                         Model model) throws UserNotFoundException {

       List<Board> boards = boardService.findBoardsByUserId(userId);
       List<TodoTask> tasks = userService.getTasksByUserId(userId);
       List<Board> boardForFilteredTasks = new ArrayList<>();
       // Filter boards by name if specified
       List<Board> filteredBoards = (boardNames != null && !boardNames.contains("all"))
               ? boards.stream()
               .filter(board -> boardNames.contains(board.getBoardName()))
               .collect(Collectors.toList())
               : boards;

       // Filter tasks by priority if specified
       List<TodoTask> filteredTasks = (priorities != null && !priorities.contains("all"))
               ? tasks.stream()
               .filter(task -> priorities.contains(task.getPriority().name()))
               .collect(Collectors.toList())
               : tasks;
       if (priorities != null && !priorities.contains("all")) {
           Map<Integer, List<TodoTask>> tasksByBoardId = tasks.stream()
                   .filter(task -> priorities.contains(task.getPriority().name()))
                   .collect(Collectors.groupingBy(TodoTask::getBoardId));

           filteredBoards = filteredBoards.stream()
                   .peek(board -> board.setTasks(tasksByBoardId.getOrDefault(board.getBoardId(), Collections.emptyList())))
                   .filter(board -> !board.getTasks().isEmpty()) // Remove boards with no tasks after filtering
                   .collect(Collectors.toList());
       }

       model.addAttribute("userBoards", filteredBoards);

       return "home :: #boardList";
   }



    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Integer boardId) {
        try {
            User currentUser = userService.getCurrentUser();

            boardService.deleteBoardById(currentUser.getId(), boardId);
        } catch (UserNotFoundException | BoardNotFoundException exception) {
            // fix this
        }
        return "redirect:/home";
    }


}
