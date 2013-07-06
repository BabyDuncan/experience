package com.babyduncan.purpleFrog.partview.components;

import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewHandler;
import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-9
 * Time: 16:03
 */
@Component
@PartialViewHandler
public class UserPartViewComponent {

    @PartialViewMapping(value = "user", parent = "sys", defaultChild = "main")
    private void fragUser(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        modelAndView.addObject("username", "this is generated in UserPartViewComponent");
    }
}
