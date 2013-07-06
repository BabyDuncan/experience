package com.babyduncan.purpleFrog.counter.internal;

import com.babyduncan.purpleFrog.counter.CounterDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-3-17 21:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context.xml"})
public class CounterDaoImplTest {

    @Autowired
    private CounterDao counterDao;

    @Test
    public void testIncr() throws Exception {
        counterDao.set("aew", 100, "1");
        counterDao.incr("aew", 5);
        System.out.println(counterDao.get("aew"));
        Assert.assertEquals("6", counterDao.get("aew"));
        System.out.println(counterDao.incr("wqe", 10));
    }

    @Test
    public void testSet() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }
}
