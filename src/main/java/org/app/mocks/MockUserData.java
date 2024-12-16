package org.app.mocks;

import jakarta.annotation.PostConstruct;
import org.app.model.User;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockUserData {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void createMockData() {
        User mockUser = new User("Lara Kroft", "lara@gmail.com", "lara");
        userService.createUser(mockUser, "pass");
    }
}
