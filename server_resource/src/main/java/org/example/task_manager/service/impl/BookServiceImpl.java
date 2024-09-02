package org.example.task_manager.service.impl;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exceptions.BookNotFoundException;
import org.example.task_manager.mapping.BookMapper;
import org.example.task_manager.mapping.TaskMapper;
import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.example.task_manager.repositry.BookRepository;
import org.example.task_manager.repositry.TaskRepository;
import org.example.task_manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final TaskRepository taskRepository;

    private final BookMapper bookMapper;
    private final TaskMapper taskMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper, BookRepository rep, TaskMapper mapper,
                           TaskRepository taskRepository) {
        this.taskMapper = mapper;
        this.bookMapper = bookMapper;

        this.bookRepository = rep;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<BookDTO> allBooks() {
        return bookMapper.booksToBookDTOs(bookRepository.findAll());
    }

    @Override
    public List<TaskDTO> getTasksByBookID(Integer bookID) {
        Optional<Book> book = bookRepository.findById(bookID);
        if (book.isPresent()) {
            List<Task> allTasksForBook = book.get().getListTasks();
            return taskMapper.tasksToTaskDTOs(allTasksForBook);
        }

        throw new BookNotFoundException("Book with ID " + bookID + " not found");
    }

    @Override
    public Optional<BookDTO> getBookById(Integer id) {
        Optional<Book> curBook = bookRepository.findById(id);
        if (curBook.isPresent()) {
            return Optional.of(bookMapper.bookToBookDTO(curBook.get()));
        }
        
        throw new BookNotFoundException("Book with id " + id + " not found");
    }

    @Override
    public BookDTO updateBook(Integer bookID, BookDTO updatingBook) {
        Optional<Book> curOptionalBook = bookRepository.findById(bookID);
        if (curOptionalBook.isPresent()) {
            Book curBook = curOptionalBook.get();
            if (updatingBook.getBookDescription() != null) {
                curBook.setBookDescription(updatingBook.getBookDescription());
            }

            if (updatingBook.getBookName() != null) {
                curBook.setBookName(updatingBook.getBookName());
            }

            bookRepository.save(curBook);
            return bookMapper.bookToBookDTO(curBook);
        }

        throw new BookNotFoundException("Book with id " + bookID + " not found");
    }

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void addTask(Integer bookID, TaskDTO aTask) {
        Optional<Book> curBook = bookRepository.findById(bookID);
        curBook.ifPresent(book -> {
            aTask.setBook(bookMapper.bookToBookDTO(book));
            aTask.setCreationDate(LocalDateTime.now());

            Task curTask = taskMapper.taskDTOToTask(aTask);
            taskRepository.save(curTask);

            List<Task> allTasks = book.getListTasks();
            allTasks.add(taskMapper.taskDTOToTask(aTask));
        });
    }

    @Override
    public void createBook(BookDTO newBook) {
        Book aBook = bookMapper.bookDTOToBook(newBook);
        if (aBook.getCreationDate() != null) {
            aBook.setUpdatedTime(LocalDateTime.now());
        } else {
            aBook.setCreationDate(LocalDateTime.now());
        }
        bookRepository.save(aBook);
    }
}
