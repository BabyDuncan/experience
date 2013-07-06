package com.babyduncan.purpleFrog.big_pipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 18:03
 */
@Controller
public class BigpipeController {

    @RequestMapping("/bigpipe")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bigpipe");
        return modelAndView;
    }

}
