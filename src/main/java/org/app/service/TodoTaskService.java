package org.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.TaskNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.app.repository.BoardRepository;
import org.app.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TodoTaskService {

    //@Autowired  creates and destroys objects for you, you don't need to instantiate them
    private final TaskRepository taskRepository;

    private final UserService userService;
    private final BoardRepository boardRepository;

    public TodoTask insertTask(TodoTask task) {
        //runs the insert into table todo_tasks values(...)
        return taskRepository.save(task);
    }

    public TodoTask moveTask(Integer taskId, Integer newBoardId)
        throws TaskNotFoundException, BoardNotFoundException {
        TodoTask toUpdateTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("No Found Task"));

        Board newBoard = boardRepository.findById(newBoardId)
            .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        toUpdateTask.setBoard(newBoard);
        newBoard.getTasks().add(toUpdateTask);
        if(newBoard.getStatus() != null){
            toUpdateTask.setStatus(newBoard.getStatus());
            newBoard.setStatus(newBoard.getStatus());
        }
        boardRepository.save(newBoard);
        log.info("Updated task: " + toUpdateTask.getTitle() + " to board: " + newBoard.getBoardName());
        return taskRepository.save(toUpdateTask);
    }

    public TodoTask updateTask(Integer taskId, String title, String description, Priority priority, LocalDate deadline, Status status, String boardName)
            throws TaskNotFoundException {

        Board newBoard = boardRepository.findBoardByBoardName(Status.valueOf(boardName).getValue());
        TodoTask toUpdateTask = taskRepository.findById(taskId) .orElseThrow(() -> new TaskNotFoundException("No Found Task"));
        toUpdateTask.setTitle(title);
        toUpdateTask.setDescription(description);
        toUpdateTask.setPriority(priority);
        toUpdateTask.setDeadline(deadline);
        toUpdateTask.setStatus(status);
        toUpdateTask.setBoard(newBoard);
        return taskRepository.save(toUpdateTask);
    }

    public TodoTask getTaskById(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("No Found Task"));
    }

    public List<TodoTask> getAllTasks() {
        return (List<TodoTask>) taskRepository.findAll();
    }

    public void deleteTaskById(Integer userId, Integer taskId) throws TaskNotFoundException {
        if (taskRepository.findById(taskId).isEmpty()) {
            throw new TaskNotFoundException("No Found Task");
        }
        taskRepository.deleteById(taskId);
    }

    public List<TodoTask> getTaskByDeadline(LocalDate deadline) throws TaskNotFoundException {
        List<TodoTask> tasks = taskRepository.findByDeadline(deadline);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }

    public List<TodoTask> getTodosByStatuses(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return (List<TodoTask>) taskRepository.findAll();
        }
        return taskRepository.findByStatusIn(statuses);
    }

    public List<TodoTask> getTasksByUserId(Integer userId) throws UserNotFoundException {
        return Optional.ofNullable(taskRepository.findTodoTaskByUserId(userId)).orElse(Collections.emptyList());
    }

    public List<TodoTask> findTasksByTitle(String title) {
        return taskRepository.findTodoTaskByTitle(title);
    }

    public int getCompletedTasksCount(List<TodoTask> allTasks)  {
        List<TodoTask> doneTasks = allTasks.stream()
            .filter(task -> task.getStatus() == Status.DONE)
            .toList();
        log.info("Number of completed tasks: {}", doneTasks.size());
        return doneTasks.size();
    }

    public List<TodoTask> getTasksByBoardId(Integer boardId) throws BoardNotFoundException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found"));
        return board.getTasks();
    }
}
