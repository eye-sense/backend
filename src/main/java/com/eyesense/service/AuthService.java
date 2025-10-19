package com.eyesense.service;

import com.eyesense.model.User;
import com.eyesense.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String userEmail = (String) request.getSession().getAttribute("userEmail");
        
        if (userEmail == null) {
            throw new RuntimeException("No authenticated user found");
        }
        
        return userRepository.findByEmail(userEmail);
    }

    public boolean isAuthenticated() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            return request.getSession().getAttribute("userEmail") != null;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}