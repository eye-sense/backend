package com.eyesense.config;

import com.eyesense.model.User;
import com.eyesense.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("user@example.com") == null) {
                userRepository.save(new User("user@example.com", "password"));
            }
        };
    }
}
