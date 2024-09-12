package com.example.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }

    @RequestMapping("/404")
    public ModelAndView handleNotFound() {
        return new ModelAndView("error/404");
    }
}


