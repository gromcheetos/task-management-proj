package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.User;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId) {
        try {
            User userToBeDeleted = userService.getUserById(userId);
            userService.deleteUserById(userId);
            return ResponseEntity.ok("Deleted user:" + userToBeDeleted);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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
