package it.korea.app_boot.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list")
    public ModelAndView  listView () {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/board/boardList");
        return view;
    }
}

   