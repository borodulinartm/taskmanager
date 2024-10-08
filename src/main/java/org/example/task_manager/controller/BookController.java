package org.example.task_manager.controller;

import jakarta.validation.Valid;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.models.Task;
import org.example.task_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("curBook")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService service) {
        bookService = service;
    }
    
    @GetMapping("/books")
    public ModelAndView getListBooks(SessionStatus status) {
        ModelAndView mView = new ModelAndView();
        mView.setViewName("book/list_books");
        mView.addObject("booksList", bookService.allBooks());

        status.setComplete();

        return mView;
    }

    @GetMapping("/books/{id}")
    public ModelAndView getBookByID(@PathVariable(name = "id") int bookID, Model model) {
        ModelAndView result = new ModelAndView();
        result.setViewName("book/selected_book");

        Optional<BookDTO> curBook = bookService.getBookById(bookID);
        List<Task> tasksForTheBook = curBook.get().getListTasks();

        // Добавляем также тестовую книгу, если вдруг открыли какую-то ерунду
        // TODO: Добавить редирект на страницу с ошибкой
        model.addAttribute("curBook", curBook.get());
        model.addAttribute("listTasks", tasksForTheBook);

        return result;
    }

    @GetMapping("/books/create")
    public ModelAndView showBookForm(Model model) {
        model.addAttribute("curBook", new BookDTO());

        ModelAndView result = new ModelAndView();
        result.setViewName("book/creation_book");

        return result;
    }

    @PostMapping("/books/create")
    public ModelAndView createNewBook(
            @Valid @ModelAttribute(name = "curBook") BookDTO createdBook,
            Errors errors, RedirectAttributes attributes
    ) {
        ModelAndView res = new ModelAndView();

        if (errors.hasErrors()) {
            res.setViewName("book/creation_book");
            return res;
        }

        // Add our book to the database
        bookService.createBook(createdBook);

        res.setViewName("redirect:/books");
        attributes.addFlashAttribute("success_text", "A book has created successfully");

        return res;
    }

    // Mapping for editing strings
    @GetMapping("/books/edit/{id}")
    public ModelAndView editBookForm(
            @PathVariable(name = "id") Integer bookID, Model model
    ) {
        ModelAndView res = new ModelAndView();
        Optional<BookDTO> curBook = bookService.getBookById(bookID);

        curBook.ifPresent(book -> model.addAttribute("curBook", book));

        res.setViewName("book/edit_book");
        return res;
    }

    @PostMapping("/books/edit/{id}")
    public ModelAndView postEditBook(
            @PathVariable(name = "id") Integer bookID,
            @Valid @ModelAttribute(name = "curBook") BookDTO curBook, Errors errors, RedirectAttributes attrs
    ) {
        ModelAndView mView = new ModelAndView();

        if (errors.hasErrors()) {
            mView.setViewName("book/edit_book");
        } else {
            mView.setViewName("redirect:/books/" + bookID);
            bookService.createBook(curBook); // Create = edit

            attrs.addFlashAttribute("success_text", "The book has edited successfully");
        }

        return mView;
    }

    @GetMapping("/books/delete/{id}")
    public ModelAndView deleteBook(
            @PathVariable(name = "id") Integer bookID, RedirectAttributes attrs
    ) {
        ModelAndView mView = new ModelAndView("redirect:/books");
        bookService.deleteBook(bookID);
        attrs.addFlashAttribute("success_text", "The book has deleted successfully");

        return mView;
    }
}
