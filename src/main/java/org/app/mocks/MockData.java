package org.app.mocks;

import static org.app.model.enums.Priority.*;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.model.enums.Priority;
import org.app.model.enums.Status;
import org.app.service.TodoTaskService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockData {

    @Autowired
    private TodoTaskService service;

    @Autowired
    private UserService userService;

    private User testUser;

    @PostConstruct
    public void createMockData() {
        User mockUser = new User("Lara Kroft", "lara@gmail.com","lara");
        testUser = userService.createUser(mockUser,"pass");

        for (int i = 0; i < 10; i++) {
            service.insertTask(createTask());
        }
    }

    private TodoTask createTask() {
        List<String> titles = List.of(
                "Complete Project Proposal",
                "Schedule Meeting with Team",
                "Research Market Trends",
                "Draft Budget Report",
                "Review Client Feedback",
                "Update Presentation Slides",
                "Organize Files and Folders",
                "Send Follow-Up Emails",
                "Brainstorm Ideas for Campaign",
                "Plan Social Media Content",
                "Test Software Updates",
                "Revise Marketing Strategy",
                "Conduct User Surveys",
                "Analyze Competitor Data",
                "Prepare for Weekly Meeting",
                "Attend Training Session",
                "Create Task Checklist",
                "Solve Technical Issue",
                "Arrange Product Demo",
                "Delegate Tasks to Team Members"
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
                "Conduct surveys to gather user feedback.",
                "Analyze data related to competitors.",
                "Prepare materials for the weekly meeting.",
                "Participate in a training session.",
                "Create a checklist of tasks to be done.",
                "Resolve a technical issue or problem.",
                "Arrange a demonstration of a product.",
                "Assign tasks to members of the team."
        );
        List<Priority> priorities = List.of(
                HIGH, MEDIUM, LOW
        );

        Random random = new Random();
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(random.nextInt(10));

        int index = random.nextInt(titles.size());
        int priorityIndex = random.nextInt(priorities.size());
        Status[] statuses = Status.values();
        int statusIndex = random.nextInt(statuses.length);
        Status randomStatus = statuses[statusIndex];
        TodoTask task = new TodoTask(titles.get(index), descriptions.get(index), priorities.get(priorityIndex), deadline, randomStatus);
        task.setUser(testUser);
        return task;
    }


}
