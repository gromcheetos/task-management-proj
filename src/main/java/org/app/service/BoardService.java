package org.app.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import org.app.exceptions.BoardNotFoundException;
import org.app.mocks.MockData;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Status;
import org.app.repository.BoardRepository;
import org.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final ProjectRepository projectRepository;

    // TODO: modify the singature of this method to accept a project object
    public Board createBoard(int newProjectId, Board board) {
        Project project = projectRepository.findById(newProjectId).orElseThrow();
        project.getBoards().add(board);
        projectRepository.save(project);
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
        return Optional.ofNullable(boardRepository.findBoardsByUserId(userId)).orElse(Collections.emptyList());
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
    public void deleteBoardById(Integer boardId) throws BoardNotFoundException {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            throw new BoardNotFoundException("No found board");
        }
        boardRepository.deleteById(boardId);
    }

    public void saveAll(List<Board> boards) {
        boardRepository.saveAll(boards);
    }

    public void createDefaultBoardForNewUsers(User user, String boardName, String description, Status status,
        boolean isDefault) {
        Board defaultBoard = new Board(boardName, description, isDefault, Status.TO_DO, new ArrayList<>(), user);
        defaultBoard.setDefault(true);
        defaultBoard.setStatus(Status.TO_DO);
        Project defaultProject = projectRepository.findProjectByProjectName(MockData.DEFAULT_PROJECT_NAME);
        createBoard(defaultProject.getProjectId(), defaultBoard);
    }

    public Board createDefaultBoardsForNewProject(String boardName) {
        Board defaultBoard = new Board(boardName, "");
        return boardRepository.save(defaultBoard);
    }

}