package org.app.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.app.model.Board;
import org.app.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public String searchTasks(Model model, String keyword)  {

        if (keyword != null) {
            List<Board> boards = searchService.performSearch(keyword);
            model.addAttribute("userBoards", boards);
        }

        return "home :: #boardList";
    }

}
