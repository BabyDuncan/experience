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
 * Time: 16:10
 */
@Component
@PartialViewHandler
public class TimelinePartViewComponent {

    @PartialViewMapping(value = "timeline", parent = "main", defaultChild = "")
    private void fragTimeline(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        //do something to modelAndView
        modelAndView.addObject("tweetOne", "this is generated in TimelinePartViewComponent + t1");
        modelAndView.addObject("tweetTwo", "this is generated in TimelinePartViewComponent + t2");
        modelAndView.addObject("tweetThree", "this is generated in TimelinePartViewComponent + t3");
//        try {
//            Thread.sleep(8000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }
}
