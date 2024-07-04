package org.app.mocks;

import static org.app.model.enums.Priority.*;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.app.model.Board;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.app.repository.BoardRepository;
import org.app.service.BoardService;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockData {

    @Autowired
    private TodoTaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    private BoardRepository boardRepository;
    private User testUser;

    @PostConstruct
    public void createMockData() {
        User mockUser = new User("Lara Kroft", "lara@gmail.com", "lara");
        testUser = userService.createUser(mockUser, "pass");

        List<Board> boards = new ArrayList<>();
         for (Status status : Status.values()) {
        Board board = new Board();
        board.setBoardName(status.toString());
        board.setDescription("This is a " + status.toString().toLowerCase() + " board.");
        board.setUser(mockUser);

        List<TodoTask> tasks = createTasksForBoard(status);
        board.setTasks(tasks);
        for (TodoTask task : tasks) {
            task.setBoard(board);
        }
        boards.add(board);
 }
        boardService.saveAll(boards);
    }

    private List<TodoTask> createTasksForBoard(Status status) {
        List<TodoTask> tasks = new ArrayList<>();
// Random random = new Random();
// int numberOfTasks = random.nextInt(5) + 3;

        int numberOfTasks = 3;

        for (int i = 0; i < numberOfTasks; i++) {
            TodoTask task = createTask(status);
            tasks.add(task);
        }

        return tasks;
    }

    private TodoTask createTask(Status status) {
        List<String> titles = List.of(
                "Complete Project Proposal",
                "Schedule Meeting with Team",
                "Research Market Trends",
                "Draft Budget Report",
                "Review Client Feedback",
                "Update Presentation Slides",
                "Organize Files and Folders",
                "Send Follow-Up Emails"
        );
        List<String> descriptions = List.of(
                "Write and finalize the project proposal document.",
                "Arrange a meeting time with the team members.",
                "Gather information about current market trends.",
                "Prepare a draft of the budget report for review.",
                "Examine feedback provided by the client.",
                "Update slides for a presentation.",
                "Organize files and folders for better management.",
                "Send follow-up emails to relevant parties.",
                "Generate ideas for an upcoming campaign.",
                "Outline and schedule social media posts.",
                "Test updates for software or systems.",
                "Review and update the marketing strategy.",
                "Conduct surveys to gather user feedback."
        );
        List<Priority> priorities = List.of(
                HIGH, MEDIUM, LOW
        );

        Random random = new Random();
        int index = random.nextInt(titles.size());
        int priorityIndex = random.nextInt(priorities.size());

        TodoTask task = new TodoTask(titles.get(index), descriptions.get(index), priorities.get(priorityIndex),
                setRandomDeadLine(), status);
        task.setUser(testUser);
        return task;
    }


    private LocalDate setRandomDeadLine(){
        Random random = new Random();
        LocalDate today = LocalDate.now();
        return today.plusDays(random.nextInt(10));
    }
}