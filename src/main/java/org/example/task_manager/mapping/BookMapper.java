package org.example.task_manager.mapping;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.models.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO bookToBookDTO(Book book);
    Book bookDTOToBook(BookDTO bookDTO);

    List<BookDTO> booksToBookDTOs(List<Book> books);
    List<Book> booksDTOToBook(List<BookDTO> bookDTOs);
}
