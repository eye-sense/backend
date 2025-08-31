package com.eyesense.service;

import com.eyesense.model.User;
import com.eyesense.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String email, String password) {
        logger.info("Authentication attempt for email: {}", email);
        User user = userRepository.findByEmail(email);
        boolean success = user != null && user.getPassword().equals(password);
        if (!success) {
            logger.warn("Authentication failed for email: {}", email);
        }
        return success;
    }
}
