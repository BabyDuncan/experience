package com.babyduncan.purpleFrog.partview.controllers;

import com.babyduncan.purpleFrog.partview.components.SysPartViewComponent;
import com.babyduncan.purpleFrog.partview.components.TimelinePartViewComponent;
import com.babyduncan.purpleFrog.partview.components.UserPartViewComponent;
import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewHandler;
import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewMapping;
import com.babyduncan.purpleFrog.partview.internal.page.VirtualPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-9
 * Time: 15:22
 */
@Controller
@PartialViewHandler
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private final VirtualPage virtualPage;

    private final String partViewDir = "partview";
    @Qualifier("sysPartViewComponent")
    @Autowired
    private SysPartViewComponent sysPartViewComponent;
    @Qualifier("userPartViewComponent")
    @Autowired
    private UserPartViewComponent userPartViewComponent;

    @Qualifier("timelinePartViewComponent")
    @Autowired
    private TimelinePartViewComponent timelinePartViewComponent;

    public HomeController() {
        this.virtualPage = new VirtualPage("home", "/WEB-INF/pages/" + partViewDir + "/");
    }

    @PostConstruct
    public void init() {
        this.virtualPage.addPartVeiw(sysPartViewComponent);
        this.virtualPage.addPartVeiw(userPartViewComponent);
        this.virtualPage.addPartVeiw(this);
        this.virtualPage.addPartVeiw(timelinePartViewComponent);
        this.virtualPage.buildPartViewPath();
    }

    @PartialViewMapping(value = "main", parent = "user", defaultChild = "timeline")
    private void fragMain(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        modelAndView.addObject("main", "this is generated in HomeController");
    }

    @RequestMapping("/home")
    public ModelAndView home(@RequestParam(value = "_context", required = false) String curContext, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = initPageFrame("timeline", curContext, request, response);
        return modelAndView;
    }

    private ModelAndView initPageFrame(String tartget, String curContext, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        String partViewPath = virtualPage.handleWithContext(tartget, curContext, modelAndView, request, response);
        modelAndView.setViewName(partViewDir + "/" + partViewPath);
        return modelAndView;
    }
}
