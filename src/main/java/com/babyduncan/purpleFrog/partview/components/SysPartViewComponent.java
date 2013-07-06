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
 * Time: 16:02
 */
@Component
@PartialViewHandler
public class SysPartViewComponent {

    @PartialViewMapping(value = "sys", parent = "", defaultChild = "user")
    private void fragSys(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        //do something to modelAndView
        modelAndView.addObject("syscontent", "this is generated in SyspartViewComponent .");
    }
}
