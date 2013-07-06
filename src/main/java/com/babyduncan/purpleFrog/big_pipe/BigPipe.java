package com.babyduncan.purpleFrog.big_pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 17:45
 */
public class BigPipe extends TagSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigPipe.class);

    public static final String START_RENDER = "startRender";
    public static final String FINISH_RENDER = "finishRender";

    public static ExecutorService exe = Executors.newFixedThreadPool(20);

    @Override
    public int doStartTag() throws JspException {
        CountDownLatch startRender = new CountDownLatch(BigpipeConstances.PAGELET_NUM);
        CountDownLatch finishRender = new CountDownLatch(BigpipeConstances.PAGELET_NUM);
        this.pageContext.getRequest().setAttribute(START_RENDER, startRender);
        this.pageContext.getRequest().setAttribute(FINISH_RENDER, finishRender);
        LOGGER.info("bigpipe started ...");
        JspWriter writer = this.pageContext.getOut();
        try {
            writer.write("<script type='text/javascript' src='" + BigpipeConstances.BIGPIPE_JS + "'></script>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        CountDownLatch countDownLatch = (CountDownLatch) this.pageContext.getRequest().getAttribute(FINISH_RENDER);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("bigpipe ended ...");
        return EVAL_PAGE;
    }
}
