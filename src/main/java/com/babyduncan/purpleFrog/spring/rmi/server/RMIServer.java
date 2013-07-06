package com.babyduncan.purpleFrog.spring.rmi.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: zgh
 * Date: 13-3-3
 * Time: 21:10
 */
public class RMIServer {

    public static void main(String... args) {
        //这就初始化了spring 容器，同时也启动了server。
        //信息: Binding service 'BookService' to RMI registry: RegistryImpl[UnicastServerRef [liveRef: [endpoint:[192.168.184.1:8899](local),objID:[0:0:0, 0]]]]
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

    }
}
