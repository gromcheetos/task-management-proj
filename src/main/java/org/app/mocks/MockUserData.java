package org.app.mocks;

import jakarta.annotation.PostConstruct;
import org.app.model.Project;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MockUserData {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BoardService boardService;

    private Project defaultProject;

    public static final String DEFAULT_PROJECT_NAME = "Default Project";

    @PostConstruct
    public void createMockData() {
        User mockUser = new User("Michael Jackson", "Michael@gmail.com", "Michael");
        List<User> mockUsers = Arrays.asList(
                new User("Mario Rossi", "mario@domain.com", "mario"),
                new User("Jane Doe", null, "jane"),
                new User("Hong gil-dong", "hong@test.com", "hong"),
                new User("Test Nulls", null, null),
            mockUser
        );

        for (User user : mockUsers) {
            userService.createUser(user, "pass"); // or use custom password logic
        }
    }
}
