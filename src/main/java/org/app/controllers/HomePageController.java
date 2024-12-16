package org.app.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.controllers.util.ProjectCommon;
import org.app.exceptions.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/")
public class HomePageController {

    private final ProjectCommon projectCommon;

    @GetMapping
    public String showHomePage(Model model)
        throws UserNotFoundException {
       return projectCommon.getHomePageUtility(model, null);
    }

}