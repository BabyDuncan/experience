package com.babyduncan.purpleFrog.spring.rmi.service.internal;

import com.babyduncan.purpleFrog.spring.rmi.model.Book;
import com.babyduncan.purpleFrog.spring.rmi.service.BookService;
import org.springframework.stereotype.Service;

/**
 * User: zgh
 * Date: 13-3-3
 * Time: 21:12
 */
@Service
public class BookServiceImpl implements BookService {
    @Override
    public String getBookString() {
        Book book = new Book();
        book.setName("babyduncan's book");
        book.setPrice(100);
        return book.getName() + book.getPrice();
    }
}
