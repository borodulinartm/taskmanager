package org.example.task_manager.controller;

import jakarta.validation.Valid;
import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exceptions.BookNotFoundException;
import org.example.task_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForBook(@PathVariable Integer id) {
        List<TaskDTO> allTasksForBook = bookService.getTasksByBookID(id);
        return ResponseEntity.ok(allTasksForBook);
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
    public BookDTO createBook(@RequestBody @Valid BookDTO bookDTO) {
        bookService.createBook(bookDTO);
        return bookDTO;
    }

    // Adding task to my book
    @PostMapping(path ="/{id}/add_task", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO addTaskToBook(@PathVariable(name = "id") Integer id, @RequestBody @Valid TaskDTO taskDTO) {
        bookService.addTask(id, taskDTO);

        return bookService.getBookById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " not found")
        );
    }

    // Update partition book
    @PatchMapping(path = "/{id}")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable(name = "id") Integer bookID,
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(bookID, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    // Delete book
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable(name = "id") Integer bookID) {
        bookService.deleteBook(bookID);
    }
}
