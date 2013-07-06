package com.babyduncan.purpleFrog.big_pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspWriter;
import java.util.concurrent.CountDownLatch;

/**
 * 异步的渲染任务
 * <p/>
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 23:36
 */
public class RenderTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderTask.class);

    private String rendername;
    private CountDownLatch start;
    private CountDownLatch finish;
    private JspWriter writer;

    public RenderTask(String rendername, CountDownLatch start, CountDownLatch finish, JspWriter writer) {
        this.rendername = rendername;
        this.start = start;
        this.finish = finish;
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            start.await();
            String content = rendername;
            try {
//                if (Integer.parseInt(rendername) == 4) {
//                    Thread.sleep(100 * 1000);
//                }
                Thread.sleep(Integer.parseInt(rendername) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (writer) {
                writer.write("<script type='text/javascript'>ii('" + rendername + "','" + content + "babyduncan" + "');</script>");
                writer.flush();
            }
            LOGGER.info("witer one js!" + "<script type='text/javascript'>ii('" + rendername + "','" + content + "babyduncan" + "');</script>");
            LOGGER.info(writer.isAutoFlush() + "");
            finish.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
