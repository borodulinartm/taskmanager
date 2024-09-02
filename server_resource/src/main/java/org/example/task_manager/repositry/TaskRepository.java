package org.example.task_manager.repositry;

import org.example.task_manager.models.Book;
import org.example.task_manager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task getTaskByBook(Book book);
}
