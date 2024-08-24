package org.example.task_manager;

import org.example.task_manager.dto.BookDTO;
import org.example.task_manager.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication(scanBasePackages = "org.example.task_manager")
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(BookService service) {
		return args -> {
			for (int i = 0; i < 10; ++i) {
				BookDTO myBook = BookDTO.builder()
						.bookName("My book #" + i)
						.bookDescription("Description of the book #" + i)
						.creationDate(LocalDateTime.now())
						.build();

				service.createBook(myBook);
			}
		};
	}
}
