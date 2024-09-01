package org.app.controllers.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.model.User;
import org.app.service.BoardService;
import org.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    private BoardService boardService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String createUser(
        @RequestParam("name") String name,
        @RequestParam("userEmail") String userEmail,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        log.info("Request received to register the user");
        User newUser = new User(name, userEmail, username);
        newUser.setBoards(boardService.getAllDefaultBoards());
        userService.createUser(newUser, password);
        return "redirect:/";
    }

}
