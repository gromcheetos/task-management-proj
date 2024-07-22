package org.app.repository;

import java.time.LocalDate;
import java.util.List;
import org.app.model.TodoTask;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TodoTask, Integer> {

    List<TodoTask> findByPriority(Priority priority);
    List<TodoTask> findByDeadline(LocalDate deadline);
    List<TodoTask> findByStatus(Status status);
    List<TodoTask> findTodoTaskByUserId(int id);
    List<TodoTask> findByStatusIn(List<Status> statuses);
}
