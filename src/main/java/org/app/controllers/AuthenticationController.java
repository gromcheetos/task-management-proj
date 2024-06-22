package org.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.app.model.User;
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
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public String createUser(
        @RequestParam("name") String name,
        @RequestParam("userEmail") String userEmail,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        log.info("Request received to register the user");
        User newUser = new User(name, userEmail, username);
        userService.createUser(newUser, password);
        return "redirect:/users/login";
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> basicLogin(@RequestParam("username") String username,
//        @RequestParam("password") String password) {
//        log.info("Request received for /users/login");
//        Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(username, password)
//        );
//        log.info("Authentication " + authentication);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        User user = (User) authentication.getPrincipal();
//
//        log.info("User details: " + user);
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("userId", user.getId());
//        responseBody.put("message", "User " + user.getUsername() + " logged in successfully");
//        return ResponseEntity.ok(responseBody);
//    }


}
