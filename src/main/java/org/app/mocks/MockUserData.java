package org.app.mocks;

import jakarta.annotation.PostConstruct;
import org.app.model.User;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MockUserData {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void createMockData() {
        //User mockUser = new User("Lara Kroft", "lara@gmail.com", "lara");
        List<User> mockUsers = Arrays.asList(
                new User("Lara Kroft", "lara@gmail.com", "lara"),
                new User("Mario Rossi", "mario@domain.com", "mario"),
                new User("Jane Doe", null, "jane"),
                new User("Hong gil-dong", "hong@test.com", "hong"),
                new User("Test Nulls", null, null)
        );
        for (User user : mockUsers) {
            userService.createUser(user, "pass"); // or use custom password logic
        }
    }
}
