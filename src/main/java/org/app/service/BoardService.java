package org.app.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import org.app.exceptions.BoardNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Status;
import org.app.repository.BoardRepository;
import org.app.repository.TaskRepository;
import org.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board updateBoard(int boardId, String boardName, String description) throws BoardNotFoundException {
        Board toUpdateBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException("No Found Board"));
        toUpdateBoard.setBoardName(boardName);
        toUpdateBoard.setDescription(description);

        return boardRepository.save(toUpdateBoard);
    }

    public List<Board> findBoardsByUserId(Integer userId) {
        return boardRepository.findBoardsByUserId(userId);
    }

    public Board findBoardById(Integer boardId) throws BoardNotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("No Found Board"));
    }

    public List<Board> getAllDefaultBoards() {
        List<Board> filteredList = new ArrayList<>();
        List<Board> allBoards = (List<Board>) boardRepository.findAll();
        for (Board board : allBoards) {
            if (board.isDefault()) {
                filteredList.add(board);
            }
        }
        return filteredList;
    }

    public List<Board> getAllBoards() {
        return (List<Board>) boardRepository.findAll();
    }

    @Transactional
    public void deleteBoardById(Integer userId, Integer boardId) throws BoardNotFoundException {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new BoardNotFoundException("No found board");
        }
        boardRepository.deleteById(boardId);
        User user = userRepository.findById(userId).get();
        user.getBoards().remove(board);
        userRepository.save(user);
    }

    public void saveAll(List<Board> boards) {
        boardRepository.saveAll(boards);
    }
}