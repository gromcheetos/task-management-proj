package org.app.mocks;

import jakarta.annotation.PostConstruct;
import java.util.Set;
import org.app.model.Board;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Roles;
import org.app.model.enums.Status;
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
        User mockUser = new User("Lara Kroft", "lara@gmail.com", "lara");
        List<User> mockUsers = Arrays.asList(
                new User("Mario Rossi", "mario@domain.com", "mario"),
                new User("Jane Doe", null, "jane"),
                new User("Hong gil-dong", "hong@test.com", "hong"),
                new User("Test Nulls", null, null),
            mockUser
        );

       // defaultProject = new Project(DEFAULT_PROJECT_NAME);

        for (User user : mockUsers) {
            userService.createUser(user, "pass"); // or use custom password logic
        }

//        defaultProject.setProjectOwner(mockUser);
//        defaultProject.setTeamMembers(Set.of(mockUser));
//        mockUser.setRoles(Roles.ADMIN);
//        for (Status status : Status.values()) {
//            Board board = boardService.createDefaultBoardsForNewProject(status.getValue());
//            defaultProject.getBoards().add(board);
//        }
//        defaultProject.setDescription("description");
//        projectService.createProject(defaultProject);

    }
}
