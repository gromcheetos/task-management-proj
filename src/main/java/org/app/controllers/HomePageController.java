package org.app.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.controllers.util.ProjectCommon;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Project;
import org.app.model.User;
import org.app.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/")
public class HomePageController implements ErrorController {

    private final ProjectCommon projectCommon;
    private final UserService userService;

    @GetMapping
    public String showHomePage(Model model, HttpSession session)
            throws UserNotFoundException, ProjectNotFoundException {
        Project currentProject = (Project) session.getAttribute("currentProject");
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && currentProject == null) {
            log.info("No project is selected. Redirecting to /project/select");
            return "redirect:/project/select";
        }
       return projectCommon.getHomePageUtility(model, currentProject);
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", status != null ? status.toString() : "Unknown");
        model.addAttribute("message", message != null ? message.toString() : "An unexpected error occurred");

        return "error/error";
    }

}