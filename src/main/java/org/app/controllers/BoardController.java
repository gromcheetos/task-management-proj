package org.app.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/home")
    public String getUserBoards(Model model) throws UserNotFoundException {

        User currentUser = userService.getCurrentUser();
        List<Board> boards;

        if (currentUser.getBoards().isEmpty()) {
            log.info("No boards found, loading a default board");
            boards = boardService.getAllDefaultBoards();
        } else {
            boards = boardService.findBoardsByUserId(currentUser.getId());
        }
        model.addAttribute("userBoards", boards);
        model.addAttribute("currentUser", currentUser);

        return "home";
    }

    @PostMapping("/board/create")
    public ResponseEntity<Board> createBoard(@RequestParam("boarName") String boardName,
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

    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Integer boardId) {
        log.info("Received request to delete board with ID: {}", boardId);
        try {
            User currentUser = userService.getCurrentUser();
            log.info("Current user: {}", currentUser.getId());
            boardService.deleteBoardById(currentUser.getId(), boardId);
            log.info("Deleted Board with ID: {}", boardId);
        } catch (UserNotFoundException | BoardNotFoundException exception) {
            // fix this
        }
        return "redirect:/home";
    }


}
