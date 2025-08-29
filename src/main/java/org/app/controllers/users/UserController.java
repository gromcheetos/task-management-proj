package org.app.controllers.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Project;
import org.app.model.User;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/users")
@Controller
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    @PostMapping("/update")
    public String updateUser(
        @RequestParam("userId") Integer userId,
        @RequestParam("name") String name,
        @RequestParam("userEmail") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        try {
            userService.updateUserById(userId, name, email, username, password);
            log.info("Updated user with id: {}", userId);
            return "redirect:/";
        } catch (UserNotFoundException exception) {
            return "redirect:/error/error";
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

    @GetMapping("/mypage")
    public String getUserPage(Model model) throws UserNotFoundException {
            User currentUser = userService.getCurrentUser();
            log.info("Current user: {}", currentUser);
            String[] parts = currentUser.getName().split(" ");
            String firstName = currentUser.getName().split(" ")[0];
            String lastName = parts.length > 1 ? parts[parts.length - 1] : "";
            Project currentProject = (Project) model.getAttribute("projectId");
            if (currentUser == null) {
                return "login-page";
            }
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("currentProject", currentProject);
            return "mypage";
    }
}
