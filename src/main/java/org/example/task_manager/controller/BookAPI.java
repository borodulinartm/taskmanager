package org.example.task_manager.controller;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/books", produces = {
        "application/json", "text/xml"
})
public class BookAPI {
    private final BookService bookService;

    @Autowired
    public BookAPI(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping
    public ResponseEntity<Iterable<BookDTO>> getListBooks() {
        Iterable<BookDTO> allBooks = bookService.allBooks();
        if (allBooks.iterator().hasNext()) {
            return ResponseEntity.ok(allBooks);
        }

        return ResponseEntity.notFound().build();
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookByID(@PathVariable(name = "id") Integer bookID) {
        Optional<BookDTO> book = bookService.getBookById(bookID);
        return book.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Creating books
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        bookService.createBook(bookDTO);
        return bookDTO;
    }

    // Adding task to my book
    @PostMapping(path ="/{id}/add_task", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO addTaskToBook(@PathVariable(name = "id") Integer id, @RequestBody TaskDTO taskDTO) {
        bookService.addTask(id, taskDTO);

        return bookService.getBookById(id).get();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable(name = "id") Integer bookID,
            @RequestBody BookDTO bookDTO) {
        BookDTO curBook = bookService.getBookById(bookID).orElse(null);
        if (curBook == null) {
            return ResponseEntity.notFound().build();
        }

        if (bookDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bookDTO.getBookDescription() != null) {
            curBook.setBookDescription(bookDTO.getBookDescription());
        }

        if (bookDTO.getBookName() != null) {
            curBook.setBookName(bookDTO.getBookName());
        }

        bookService.createBook(curBook);

        return new ResponseEntity<>(curBook, HttpStatus.OK);
    }
}
