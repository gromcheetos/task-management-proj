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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
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

    @GetMapping("/filter/boardName")
    public String filterByBoardName(@RequestParam("boardName") List<String> boardNames,
                                    @RequestParam("userId") int userId,
                                    Model model) throws UserNotFoundException {

            User currentUser = userService.getCurrentUser();
            List<Board> boards = boardService.findBoardsByUserId(currentUser.getId());
            List<Board> filteredBoards;

            if (boardNames.contains("all")) {
                filteredBoards = boards;
            } else {
                filteredBoards = boards.stream()
                        .filter(board -> boardNames.contains(board.getBoardName()))
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
