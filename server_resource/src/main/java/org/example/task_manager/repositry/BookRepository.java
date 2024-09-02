package org.example.task_manager.repositry;

import java.util.List;

import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
