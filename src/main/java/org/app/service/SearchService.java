package org.app.service;

import java.util.List;
import java.util.stream.Collectors;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private TodoTaskService todoTaskService;
    private BoardService boardService;

    public List<Board> performSearch(String keyword) {

        // TODO: to be implemented
        return null;
    }

    public List<Board> filterBoardsByBoardNames(List<String> boardNames, List<Board> boards) {
        return (boardNames != null && !boardNames.contains("all"))
            ? boards.stream()
            .filter(board -> boardNames.contains(board.getBoardName()))
            .collect(Collectors.toList())
            : boards;
    }

    public List<TodoTask> filterTasksByPriority(List<String> priorities, List<TodoTask> tasks) {
        return (priorities != null && !priorities.contains("all"))
            ? tasks.stream()
            .filter(task -> priorities.contains(task.getPriority().name()))
            .collect(Collectors.toList())
            : tasks;
    }

}
