package com.babyduncan.purpleFrog.counter;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-3-17 23:12
 */
public class CounterTest {

    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            if (i == 12 || i == 23 || i == 35) {
                Thread.sleep(1000);
            }
            Counter.count("1.1.1.1");
            System.out.println(Counter.checkFreq("1.1.1.1"));
        }

    }
}
