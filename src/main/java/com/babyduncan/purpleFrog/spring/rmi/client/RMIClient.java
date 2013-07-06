package com.babyduncan.purpleFrog.spring.rmi.client;

import com.babyduncan.purpleFrog.spring.rmi.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * User: zgh
 * Date: 13-3-3
 * Time: 21:10
 */
public class RMIClient {


    public static void main(String[] args) {
        //使用依赖注入的方式
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client-application-context.xml");
        BookService bookService = (BookService) context.getBean("rmiClient");
        System.out.println(bookService.getBookString());
        //自建factory的方式
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceInterface(BookService.class);
        factory.setServiceUrl("rmi://192.168.184.1:8899/BookService");
        factory.afterPropertiesSet();

        BookService bookServicee = (BookService) factory.getObject();
        System.out.println(bookServicee.getBookString());
    }

}
