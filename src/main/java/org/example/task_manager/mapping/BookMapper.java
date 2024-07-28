package org.example.task_manager.mapping;

import org.mapstruct.Mapper;

import java.util.List;

import org.example.task_manager.dto.*;
import org.example.task_manager.models.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO bookToBookDTO(Book book);
    Book bookDTOToBook(BookDTO bookDTO);

    List<BookDTO> booksToBookDTOs(List<Book> books);
    List<Book> booksDTOToBook(List<BookDTO> bookDTOs);
}
