package com.babyduncan.purpleFrog.tagSupport;

import com.babyduncan.purpleFrog.spring.rmi.model.Book;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-5
 * Time: 13:11
 */
public class BookTag extends TagSupport {

    private Book book;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        try {
            out.write("tag started!!!");
            out.write(book.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;

    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        try {
            out.write("tag ended!!!");
            out.write(book.toString() + "end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;

    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
