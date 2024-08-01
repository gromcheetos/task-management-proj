package org.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import org.app.exceptions.BoardNotFoundException;
import org.app.exceptions.TaskNotFoundException;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TodoTaskService {
    //@Autowired  creates and destroys objects for you, you don't need to instantiate them
    private final TaskRepository taskRepository;

    private final  UserService userService;

    public TodoTask insertTask(TodoTask task){
        return taskRepository.save(task); //runs the insert into table todo_tasks values(...)
    }
    public TodoTask updateTask(Integer taskId, TodoTask todoTask) throws TaskNotFoundException {
        TodoTask toUpdateTask = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("No Found Task"));
        toUpdateTask.setTitle(todoTask.getTitle());
        toUpdateTask.setDescription(todoTask.getDescription());
        toUpdateTask.setDeadline(todoTask.getDeadline());
        toUpdateTask.setPriority(todoTask.getPriority());
        toUpdateTask.setStatus(todoTask.getStatus());
        return taskRepository.save(toUpdateTask);
    }
    public TodoTask getTaskById(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("No Found Task"));
    }
    public List<TodoTask> getAllTasks(){
        return (List<TodoTask>)taskRepository.findAll();
    }
    public void deleteTaskById(Integer userId, Integer taskId)  throws TaskNotFoundException {
        if(taskRepository.findById(taskId).isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        taskRepository.deleteById(taskId);

    }

    public List<TodoTask> getTaskByPriority(Priority priority) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByPriority(priority);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
    public List<TodoTask> getTaskByDeadline(LocalDate deadline) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByDeadline(deadline);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
    public List<TodoTask> getTaskByStatus(Status status) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByStatus(status);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
   public long getCount(){
        return taskRepository.count();
   }

    public List<TodoTask> getTodosByStatuses(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return (List<TodoTask>) taskRepository.findAll();
        }
        return taskRepository.findByStatusIn(statuses);
    }

}
