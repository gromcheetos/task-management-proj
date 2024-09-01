package org.app.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TodoTaskService todoTaskService;
    private final BoardService boardService;

    public List<Board> performSearch(String keyword) {

        List<Board> boardsContainingKeyword = findBoardsContainingKeyword(keyword);
        List<TodoTask> tasksContainingKeyword = findTasksContainingKeyword(keyword);

        if (boardsContainingKeyword.isEmpty() && tasksContainingKeyword.isEmpty()) {
            // no boards or tasks match the keyword user entered in the search bar
            return null;
        } else if (!boardsContainingKeyword.isEmpty()) {
            return boardsContainingKeyword;
        }

        return null;
    }

    public List<Board> filterBoardsByBoardNames(List<String> boardNames, List<Board> boards) {
        return (boardNames != null && !boardNames.contains("all"))
            ? boards.stream()
            .filter(board -> boardNames.contains(board.getBoardName()))
            .toList() : boards;
    }

    public List<TodoTask> filterTasksByPriority(List<String> priorities, List<TodoTask> tasks) {
        return (priorities != null && !priorities.contains("all"))
            ? tasks.stream()
            .filter(task -> priorities.contains(task.getPriority().name()))
            .toList() : tasks;
    }

    private List<TodoTask> findTasksContainingKeyword(String keyword) {
        return todoTaskService.getAllTasks().stream()
            .filter(task -> (task.getTitle().contains(keyword) || task.getDescription().contains(keyword))
                && !task.getBoard().isDefault())
            .toList();
    }

    private List<Board> findBoardsContainingKeyword(String keyword) {
        return boardService.getAllBoards().stream()
            .filter(board -> (board.getBoardName().contains(keyword) || board.getDescription().contains(keyword))
                && !board.isDefault())
            .toList();
    }

}
