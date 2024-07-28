package org.example.task_manager.service.impl;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.mapping.BookMapper;
import org.example.task_manager.mapping.TaskMapper;
import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.example.task_manager.repositry.BookRepository;
import org.example.task_manager.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final TaskMapper taskMapper;

    // @Autowired
    public BookServiceImpl(BookMapper bookMapper, BookRepository rep, TaskMapper mapper) {
        this.taskMapper = mapper;
        this.bookMapper = bookMapper;
        this.bookRepository = rep;
    }

    @Override
    public List<BookDTO> allBooks() {
        return bookMapper.booksToBookDTOs(bookRepository.findAll());
    }

    @Override
    public Optional<BookDTO> getBookById(Integer id) {
        Optional<Book> curBook = bookRepository.findById(id);
        if (curBook.isPresent()) {
            return Optional.of(bookMapper.bookToBookDTO(curBook.get()));
        }
        
        return Optional.ofNullable(null);
    }

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void addTask(Integer bookID, TaskDTO aTask) {
        Optional<Book> curBook = bookRepository.findById(bookID);
        curBook.ifPresent(book -> {
            List<Task> allTasks = book.getListTasks();
            allTasks.add(taskMapper.taskDTOToTask(aTask));
        });
    }

    @Override
    public void createBook(BookDTO newBook) {
        Book aBook = bookMapper.bookDTOToBook(newBook);
        bookRepository.save(aBook);
    }
}
