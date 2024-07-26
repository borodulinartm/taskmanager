package org.example.task_manager.controller;

import org.example.task_manager.dao.BookDAO;
import org.example.task_manager.models.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@SessionAttributes("curBook")
public class BookController {
    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO dao) {
        bookDAO = dao;
    }
    
    @GetMapping("/books")
    public ModelAndView getListBooks(SessionStatus status) {
        ModelAndView mView = new ModelAndView();
        mView.setViewName("book/list_books");
        mView.addObject("booksList", bookDAO.getBooks());

        status.setComplete();

        return mView;
    }

    @GetMapping("/books/{id}")
    public ModelAndView getBookByID(@PathVariable(name = "id") int bookID, Model model) {
        ModelAndView result = new ModelAndView();
        result.setViewName("book/selected_book");

        Book curBook = bookDAO.getBookByID(bookID);
        model.addAttribute("curBook", curBook);

        if (curBook == null) {
            log.error("Current book is null");
        }

        return result;
    }
}
