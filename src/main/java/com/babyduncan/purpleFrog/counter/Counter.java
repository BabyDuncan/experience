package com.babyduncan.purpleFrog.counter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-3-17 21:06
 */

public class Counter {

    private static CounterDao counterDao = (CounterDao) new ClassPathXmlApplicationContext("application-context.xml").getBean("counterDaoImpl");

    //频率控制的几个要素  时间段 时间颗粒 频率控制的数值
    public static final int TIME_OUT = 60 * 5;
    public static final int SLABMILS = 100;
    public static final int MAX_TIME = 20;

    public static void count(String ip) {
        long now = (System.currentTimeMillis() / 1000) / SLABMILS;
        long l = counterDao.incr(ip + now, 1l);
        if (l == -1) {
            counterDao.set(ip + now, TIME_OUT, 1 + "");
        }
    }

    public static boolean checkFreq(String ip) {
        int total = getCount(ip);
        if (total > MAX_TIME) {
            return false;
        }
        return true;
    }


    public static int getCount(String ip) {
        long now = (System.currentTimeMillis() / 1000) / SLABMILS;
        int total = 0;
        for (int i = 0; i < (TIME_OUT / SLABMILS); i++) {
            if (counterDao.get(ip + "" + (now - i)) != null) {
                System.out.println("SLAB " + i + "values" + counterDao.get(ip + "" + (now - i)).toString());
                total += Integer.parseInt(counterDao.get(ip + "" + (now - i)).toString());
            }
        }
        return total;
    }

}
