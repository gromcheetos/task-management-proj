package org.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class PagesHandlerController {

    @GetMapping("/signup")
    public String showRegistrationPage() {
        return "registration";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login-page";
    }
}
