package com.babyduncan.purpleFrog.counter;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-13
 * Time: 23:23
 */
public interface CounterDao {
    /**
     * atom plus
     *
     * @param key
     * @param v
     * @return -1 no value
     */
    public long incr(String key, long v);

    /**
     * set a key-value with expireTime
     *
     * @param key
     * @param timeout
     * @param v
     */
    public void set(String key, int timeout, String v);

    /**
     * get value by key
     *
     * @param key
     * @return
     */
    public Object get(String key);
}
