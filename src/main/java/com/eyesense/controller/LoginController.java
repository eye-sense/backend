package com.eyesense.controller;

import com.eyesense.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginPage() {
        return ResponseEntity.ok(Map.of("message", "Login endpoint ready"));
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password,
                                   HttpSession session) {
        try {
            if (loginService.authenticate(email, password)) {
                session.setAttribute("userEmail", email);
                return ResponseEntity.ok().body(Map.of("success", true, "email", email));
            } else {
                return ResponseEntity.status(401).body(Map.of("success", false, "error", "Email or password incorrect"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", "Erro interno: " + ex.getMessage()));
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
