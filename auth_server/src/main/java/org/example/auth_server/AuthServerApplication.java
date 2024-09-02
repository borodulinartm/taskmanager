package org.example.auth_server;

import org.example.auth_server.user.User;
import org.example.auth_server.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			userRepository.save(User
					.builder()
					.username("hamuba")
					.password(passwordEncoder.encode("password"))
					.role("ROLE_ADMIN")
					.build());

			userRepository.save(
					User.builder()
							.username("tacochef")
							.password(passwordEncoder.encode("password"))
							.role("ROLE_ADMIN")
							.build()
			);
		};
	}
}
