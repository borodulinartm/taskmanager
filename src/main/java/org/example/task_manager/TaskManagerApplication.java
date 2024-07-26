package org.example.task_manager;

import org.example.task_manager.models.Book;
import org.example.task_manager.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(BookService service) {
		return args -> {
			for (int i = 0; i < 10; ++i) {
				Book myBook = Book.builder()
						.bookName("My book #" + i)
						.bookDescription("Description of the book #" + i)
						.creationDate(LocalDateTime.now())
						.createdUser("Artem")
						.build();

				service.createBook(myBook);
			}
		};
	}
}
