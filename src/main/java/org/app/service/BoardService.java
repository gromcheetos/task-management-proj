package org.app.service;

import org.app.exceptions.BoardNotFoundException;

import org.app.exceptions.TaskNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;

import org.app.model.TodoTask;
import org.app.repository.BoardRepository;
import org.app.repository.TaskRepository;
import org.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

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

    public List<Board> findBoardsByUserId(Integer userId) throws UserNotFoundException {
        return boardRepository.findBoardsByUserId(userId);
    }

    public Board findBoardById(Integer boardId) throws BoardNotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("No Found Board"));
    }

    public List<Board> getAllDefaultBoards() {
        return boardRepository.findAllByIsDefault(true);
    }

    public void deleteBoardById(Integer boardId) throws BoardNotFoundException {
        if(boardRepository.findById(boardId).isEmpty()){
            throw new BoardNotFoundException("No found board");
        }
        boardRepository.deleteById(boardId);
    }


    public void saveAll(List<Board> boards) {
        boardRepository.saveAll(boards);
    }
}
