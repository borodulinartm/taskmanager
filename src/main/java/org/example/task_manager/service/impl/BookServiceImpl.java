package org.example.task_manager.service.impl;

import org.example.task_manager.models.Book;
import org.example.task_manager.repositry.BookRepository;
import org.example.task_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository rep) {
        bookRepository = rep;
    }

    @Override
    public Iterable<Book> allBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void createBook(Book newBook) {
        bookRepository.save(newBook);
    }
}
