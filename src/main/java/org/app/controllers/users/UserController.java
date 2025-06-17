package org.app.controllers.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.User;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(
        @RequestParam("userId") Integer userId,
        @RequestParam("name") String name,
        @RequestParam("userEmail") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        try {
            User updatedUser = userService.updateUserById(userId, name, email, username, password);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list/{projectId}")
    public ResponseEntity<Set<User>> getAllUsers(@PathVariable("projectId") Integer projectId) throws UserNotFoundException {
        Set<User> userList =  projectService.getTeamMembers(projectId);
        return ResponseEntity.ok(userList);

    }

    @GetMapping("/search")
    public ResponseEntity<User> getUserById(@RequestParam("userId") Integer userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
