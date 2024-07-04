package org.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String getUserBoards(Model model) throws UserNotFoundException {

// Load default boards
        List<Board> boards = boardService.getAllDefaultBoards();
        model.addAttribute("boards", boards);
        log.info("Default Boards: " + Arrays.toString(boards.toArray()));

// Get the current authenticated user
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            log.info("No authenticated user found");
            return "home";
        }
        List<Board> userBoards = boardService.findBoardsByUserId(currentUser.getId());
        model.addAttribute("userBoards", userBoards);
        log.info("User boards: " + Arrays.toString(userBoards.toArray()));
        return "home";
    }
    @PostMapping("/board/create")
    public ResponseEntity<Board> createBoard(@RequestParam("boarName") String boardName,
                                               @RequestParam("description") String description){
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Board board = new Board(boardName, description);
        board.setUser(currentUser);
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    @PostMapping("/board/update")
    public ResponseEntity<Board> updateBoard(
            @RequestParam("boardId") Integer boardId,
            @RequestParam("boarName") String boardName,
            @RequestParam("description") String description) {
        try {
            Board updateBoard = boardService.updateBoard(boardId, boardName, description);
            return ResponseEntity.ok(updateBoard);
        } catch (BoardNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }
    @DeleteMapping("/board/delete")
    public ResponseEntity<?> deleteBoard(@RequestParam("boardId") Integer boardId,
                                         @RequestParam("userId") Integer userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }

            Board boardToBeDeleted = boardService.deleteBoardById(boardId);
            if (boardToBeDeleted == null) {
                throw new BoardNotFoundException("Board not found with id: " + boardId);
            }

            return ResponseEntity.ok("Deleted Board: " + boardToBeDeleted);
        } catch (UserNotFoundException | BoardNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId) {
        try {
            User userToBeDeleted = userService.getUserById(userId);
            if (userToBeDeleted == null) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }
            userService.deleteUserById(userId);
            return ResponseEntity.ok("Deleted user:" + userToBeDeleted);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
