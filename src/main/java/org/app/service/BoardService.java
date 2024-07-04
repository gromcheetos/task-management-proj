package org.app.service;

import org.app.exceptions.BoardNotFoundException;

import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;

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

    public Board createBoard(Board board){
        return boardRepository.save(board);

    }

    public Board updateBoard(int boardId, String boardName, String description) throws BoardNotFoundException {
        Board toUpdateBoard = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("No Found Board"));
        toUpdateBoard.setBoardName(boardName);
        toUpdateBoard.setDescription(description);

        return boardRepository.save(toUpdateBoard);
    }
    public List<Board> findBoardsByUserId(Integer userId) throws UserNotFoundException{
        return boardRepository.findBoardsByUserId(userId);
    }
    public List<Board> getAllDefaultBoards(){
        return (List<Board>)boardRepository.findAll();
    }


    public Board deleteBoardById(Integer boardId) throws BoardNotFoundException {
        Optional<Board> boardOpt = boardRepository.findById(boardId);
        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            boardRepository.delete(board);
            return board;
        } else {
            throw new BoardNotFoundException("Board not found with id: " + boardId);
        }
    }


    public void saveAll(List<Board> boards) {
        boardRepository.saveAll(boards);
    }
}
