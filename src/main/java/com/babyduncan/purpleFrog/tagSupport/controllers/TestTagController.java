package com.babyduncan.purpleFrog.tagSupport.controllers;

import com.babyduncan.purpleFrog.spring.rmi.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 16:10
 */
@Controller
@RequestMapping("/testTag")
public class TestTagController {

    @RequestMapping("/test")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("testTag");
        Book book = new Book();
        book.setName("babyduncan's empire");
        book.setPrice(9999);
        modelAndView.addObject("book", book);
        return modelAndView;
    }

}
