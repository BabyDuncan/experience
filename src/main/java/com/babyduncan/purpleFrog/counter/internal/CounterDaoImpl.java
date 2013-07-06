package com.babyduncan.purpleFrog.counter.internal;

import com.babyduncan.purpleFrog.counter.CounterDao;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-13
 * Time: 23:23
 */
@Service
public class CounterDaoImpl implements CounterDao {

    private Jedis jedis = new Jedis("localhost", 6379);

    @Override
    public long incr(String key, long v) {
        if (!jedis.exists(key)) {
            System.out.println(key + "not exists!");
            return -1;
        }
        return jedis.incrBy(key, v);
    }

    @Override
    public void set(String key, int timeout, String v) {
        jedis.setex(key, timeout, v);
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    public static void main(String... args) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.setex("foo2", 1000, "1");
        jedis.incr("foo2");
        jedis.incrBy("foo2", 100);
        System.out.println(jedis.get("foo2"));
    }
}
