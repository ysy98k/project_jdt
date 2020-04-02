package com.raising.forward.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/raising/")
public class FrameRaisingController {

    @RequestMapping(value = "frame.do")
    public ModelAndView frame(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/raising/frame");
    }
}
