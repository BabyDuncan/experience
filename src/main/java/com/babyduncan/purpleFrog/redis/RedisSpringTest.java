package com.babyduncan.purpleFrog.redis;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-14
 * Time: 00:36
 */
public class RedisSpringTest {
    private ApplicationContext app;
    private RedisTemplate redisTemplate;

    @Before
    public void init() {
        app = new ClassPathXmlApplicationContext("application-context.xml");
        redisTemplate = (RedisTemplate) app.getBean("redisTemplate");
    }

    @Test
    public void test() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("zgh".getBytes(), "helloredis".getBytes());
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}
