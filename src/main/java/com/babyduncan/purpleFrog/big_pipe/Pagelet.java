package com.babyduncan.purpleFrog.big_pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 17:45
 */
public class Pagelet extends TagSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pagelet.class);

    private String name;

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write("<div id='" + name + "'>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        final JspWriter writer = pageContext.getOut();
        CountDownLatch start_render = (CountDownLatch) this.pageContext.getRequest().getAttribute("startRender");
        try {
            writer.write("</div>");
            writer.flush();
            start_render.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CountDownLatch finish_render = (CountDownLatch) request.getAttribute("finishRender");

        BigPipe.exe.execute(new RenderTask(name, start_render, finish_render, writer));

        return super.doEndTag();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
