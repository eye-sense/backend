package com.eyesense.controller;

import com.eyesense.dto.UploadHistoryDTO;
import com.eyesense.model.User;
import com.eyesense.repository.ObjectRepository;
import com.eyesense.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final ObjectRepository objectRepository;
    private final UserRepository userRepository;

    public HistoryController(ObjectRepository objectRepository, UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUserHistory(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        List<UploadHistoryDTO> history = objectRepository.findByUserId(user.getId())
                .stream()
                .map(obj -> {
                    var result = obj.getResult();
                    UploadHistoryDTO dto = new UploadHistoryDTO();
                    dto.setObjectId(obj.getId());
                    dto.setModelVersion(result != null ? result.getModelVersion() : null);
                    dto.setCatarataProbability(result != null ? result.getCataractProbability() : null);
                    dto.setGlaucomaProbability(result != null ? result.getGlaucomaProbability() : null);
                    dto.setHealthyProbability(result != null ? result.getHealthyProbability() : null);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(history);
    }
}
